package dan.tp2021.danmsusuarios.rest;

import java.util.List;


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
import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraNotFoundException;
import dan.tp2021.danmsusuarios.service.ObraService;

@RestController
@RequestMapping("/api/obra")
public class ObraRest {

	@Autowired
	ObraService obraServiceImpl;

	@GetMapping()
	public ResponseEntity<List<Obra>> todos(@RequestParam(required = false, defaultValue = "0") Integer idCliente,
			@RequestParam(required = false, defaultValue = "") String tipoObra,
			@RequestParam(required = false, defaultValue = "") String cuitCliente) {

		try {
			return ResponseEntity.ok(obraServiceImpl.getObraByParams(tipoObra, idCliente, cuitCliente));
		} catch (ObraNotFoundException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Obra> obraPorId(@PathVariable(name = "id") Integer id) {
		try {
			return ResponseEntity.ok(obraServiceImpl.getObraById(id));
		} catch (ObraNotFoundException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PostMapping()
	public ResponseEntity<Obra> crear(@RequestBody() Obra nuevo) {

		// TODO ver que pasa con el cliente. Hay que crear uno nuevo o verificar que
		// existe el recibido? Se recibe un cliente completo un ID?
		// Crear una obra con un objeto cliente dentro no está funcionando, creo que
		// tendríamos que recibir el id de cliente nomás y buscar al cliente por ID en
		// el service.
		try {
			return ResponseEntity.ok(obraServiceImpl.saveObra(nuevo));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PutMapping("/{id}")
	public ResponseEntity<Obra> actualizar(@PathVariable(name = "id") Integer id, @RequestBody Obra nuevo) {
		try {
			return ResponseEntity.ok(obraServiceImpl.actualizarObra(id, nuevo));
		} catch (ObraNotFoundException e) {
			return ResponseEntity.badRequest().build();
		} catch (ObraForbiddenException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Obra> borrar(@PathVariable(name = "id") Integer id) {
		try {
			return ResponseEntity.ok(obraServiceImpl.deleteObraById(id));
		} catch (ObraNotFoundException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

}
