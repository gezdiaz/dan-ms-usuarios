package dan.tp2021.danmsusuarios.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

import dan.tp2021.danmsusuarios.domain.Empleado;
import dan.tp2021.danmsusuarios.domain.TipoUsuario;
import dan.tp2021.danmsusuarios.domain.Usuario;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoNotFoundException;
import dan.tp2021.danmsusuarios.service.EmpleadoService;

@RestController
@RequestMapping("/api/empleado")
public class EmpleadoRest {

	private static final Logger logger = LoggerFactory.getLogger(EmpleadoRest.class);

	@Autowired
	EmpleadoService empleadoServiceImpl;

	@GetMapping()
	public ResponseEntity<List<Empleado>> todos(
			@RequestParam(name = "nombre", required = false, defaultValue = "") String nombre) {

		try {
			return ResponseEntity.ok(empleadoServiceImpl.getEmpleadosByParams(nombre));
		} catch (Exception e) {
			logger.error("todos(): Se produjo un error al obtener los empleados", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Empleado> empleadoPorId(@PathVariable(name = "id") Integer id) {

		try {
			return ResponseEntity.ok(empleadoServiceImpl.getEmpleadoById(id));
		} catch (EmpleadoNotFoundException e) {
			logger.warn("empleadoPorId(): No se encontró el empleado con id: " + id, e);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("empleadoPorId(): Se produjo un error al obtener el empleado con id: " + id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping()
	public ResponseEntity<Empleado> crear(@RequestBody() Empleado nuevo) {

		if (nuevo != null && nuevo.getUser() != null && !nuevo.getEmail().isBlank() && !nuevo.getNombre().isBlank()) {
			try {
				return ResponseEntity.status(HttpStatus.CREATED).body(empleadoServiceImpl.saveEmpleado(nuevo));
			} catch (Exception e) {
				logger.error("empleadoPorId(): Se produjo un error al crear el empleado.", e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
		logger.warn("crear(): No se puede crear el empleado: " + nuevo);
		return ResponseEntity.badRequest().build(); // Datos incompletos.
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Empleado> actualizar(@PathVariable(name = "id") Integer id, @RequestBody() Empleado nuevo) {

		try {
			return ResponseEntity.ok(empleadoServiceImpl.actualizarEmpleado(id, nuevo));
		} catch (EmpleadoNotFoundException e) {
			logger.warn("actualizar(): No se encontró el empleado a actualizar.", e);
			return ResponseEntity.notFound().build(); //No existe empleado con ese id/
		} catch (EmpleadoForbiddenException e) {
			logger.warn("actualizar(): El id recibido no coincide con el del empleado.", e);
			return ResponseEntity.badRequest().build(); //Id de la url y de "nuevo" distintos
		} catch (Exception e) {
			logger.error("actualizar(): Se produjo un error al actualizar el empelado.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Empleado> borrar(@PathVariable(name = "id") Integer id) {
		
		try {
			return ResponseEntity.ok(empleadoServiceImpl.deleteEmpleadoById(id));
		} catch (EmpleadoNotFoundException e) {
			logger.warn("borrar(): No se encontró el empleado a eliminar.", e);
			return ResponseEntity.notFound().build(); //No existe empleado con ese id/
		} catch (Exception e) {
			logger.error("borrar(): Se produjo un error al eliminar el empelado.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}
