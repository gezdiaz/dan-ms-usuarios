package dan.tp2021.danmsusuarios.rest;

import java.util.List;
import dan.tp2021.danmsusuarios.service.ClienteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNoHabilitadoException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException;
import dan.tp2021.danmsusuarios.exceptions.obra.TipoNoValidoException;

@RestController
@RequestMapping("/api/cliente")
@Api(value = "ClienteRest", description = "Permite gestionar los clientes de la empresa")
public class ClienteRest {

	private static final Logger logger = LoggerFactory.getLogger(ClienteRest.class);

	@Autowired
	private ClienteService clienteServiceImpl;

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Busca un cliente por id")
	public ResponseEntity<Cliente> clientePorId(@PathVariable Integer id) {

		try {
			return ResponseEntity.ok(clienteServiceImpl.getClienteById(id));
		} catch (dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException e) {
			logger.warn("clientePorId(): No se encontró un cliente con id " + id);
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("clientePorId(): Error al buscar el cliente con id " + id + ": " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@GetMapping
	public ResponseEntity<List<Cliente>> todos(
			@RequestParam(required = false, name = "razonSocial", defaultValue = "") String razonSocial) {

		try {
			List<Cliente> resultado = clienteServiceImpl.getClientesByParams(razonSocial);
			logger.debug("GET /api/cliente respondiendo con lista de clientes: " + resultado);
			return ResponseEntity.ok(resultado);
		} catch (Exception e) {
			logger.error("Se produjo un error al filtrar clientes por razón social " + razonSocial + ": " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 4.a.i GET cliente by cuit
	@GetMapping(path = "/cuit/{cuit}")
	public ResponseEntity<Cliente> clientePorCuit(@PathVariable(name = "cuit") String cuit) {

		try {
			return ResponseEntity.ok(clienteServiceImpl.getClienteByCuit(cuit));
		} catch (ClienteNotFoundException e) {
			logger.warn("clientePorCuit(): No se encontró un cliente con cuit: " + cuit, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("clientePorCuit(): Error al buszcar el cliente con cuit " + cuit + ": " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping
	public ResponseEntity<Cliente> crear(@RequestBody Cliente nuevo) {
		System.out.println(" crear cliente " + nuevo);

		if (nuevo.getUser().getPassword() != null && nuevo.getUser().getUser() != null && nuevo.getObras() != null
				&& nuevo.getObras().size() > 0) {
			try {
				Cliente guardado = clienteServiceImpl.saveCliente(nuevo);
				return ResponseEntity.ok(guardado);
			} catch (ClienteNoHabilitadoException e) {
				// El cliente no esta habilitado, retorno 400 porque es un error de los datos,
				// no es un error del servidor.TODO ver si puede ser un 403 FORBIDDEN
				logger.warn("Cliente no habilitado. Mensaje de la excepción: " + e.getMessage(), e);
				return ResponseEntity.badRequest().build();
			} catch (TipoNoValidoException e) {
				logger.warn("Error al guardar tipo de obra: " + e.getMessage(), e);
				return ResponseEntity.unprocessableEntity().build();
			} catch (Exception e) {
				// 500 internal server error, porque es un eror del servidor, también podría ser
				// un 503 (Servicio no disponible) o un 502 (GBad gateway)
				logger.error("Error al guardar el cliente. Mensaje de la excepción: " + e.getMessage(), e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
		// Si los datos recibidos no cumplen las condiciones -> BAD REQUEST, no hay que
		// enviar el cliente porque no es válido.
		return ResponseEntity.badRequest().build();
	}

	@PutMapping(path = "/{id}")
	@ApiOperation(value = "Actualiza un cliente")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Actualizado correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"), @ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El ID no existe") })
	public ResponseEntity<Cliente> actualizar(@RequestBody Cliente nuevo, @PathVariable Integer id) {
		
		try {
			logger.debug("actualizar(): Se va a actualizar al cliente con id: \"" + id + "\" con los datos: " + nuevo);
			return ResponseEntity.ok(clienteServiceImpl.actualizarCliente(id, nuevo));
		} catch (ClienteNotFoundException e) {
			logger.warn("actualizar(): No se encontró el cliente con id " + id, e);
			return ResponseEntity.notFound().build();
		} catch (ClienteNoHabilitadoException e) {
			logger.warn("actualizar(): El id recibido en el path no coincide con el recibido en el body: path " + id + " body: " + nuevo, e);
			return ResponseEntity.badRequest().build();
		} catch (TipoNoValidoException e) {
			logger.warn("actualizar(): Error al validar tipos de obra: "+ e.getMessage());
			return ResponseEntity.unprocessableEntity().build();
		} catch (Exception e) {
			logger.error("actualizar(): Ocurrió un error al actualizar el cliente: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Cliente> borrar(@PathVariable Integer id) {
		// TODO ver, borra bien clientes sin pedidos pero tira un error 500 al devolver
		//  el JSON
		try {
			Cliente eliminado = clienteServiceImpl.darDeBaja(id);
			return ResponseEntity.ok(eliminado);
		} catch (ClienteNotFoundException e) {
			// El cliente no esta habilitado, retorno 400 porque es un error de los datos,
			// no es un error del servidor.TODO ver si puede ser un 403 FORBIDDEN
			System.out.println("Error Cliente a eliminar no encontrado. Mensaje de la excepción: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		} catch (ClienteException e) {
			// 500 internal server error, porque es un eror del servidor, también podría ser
			// un 503 (Servicio no disponible) o un 502 (GBad gateway)
			System.out.println("Error al dar de baja el cliente. Mensaje de la excepción: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
