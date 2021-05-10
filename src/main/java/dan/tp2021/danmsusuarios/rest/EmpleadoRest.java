package dan.tp2021.danmsusuarios.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	@Autowired
	EmpleadoService empleadoServiceImpl;

	@GetMapping()
	public ResponseEntity<List<Empleado>> todos(
			@RequestParam(name = "nombre", required = false, defaultValue = "") String nombre) {

		try {
			return ResponseEntity.ok(empleadoServiceImpl.getEmpleadosByParams(nombre));
		} catch (EmpleadoNotFoundException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Empleado> empleadoPorId(@PathVariable(name = "id") Integer id) {

		try {
			return ResponseEntity.ok(empleadoServiceImpl.getEmpleadoById(id));
		} catch (EmpleadoNotFoundException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping()
	public ResponseEntity<Empleado> crear(@RequestBody() Empleado nuevo) {

		if (nuevo != null && nuevo.getUser() != null && !nuevo.getEmail().isBlank() && !nuevo.getNombre().isBlank()) {
			try {
				return ResponseEntity.ok(empleadoServiceImpl.saveEmpleado(nuevo));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}

		return ResponseEntity.badRequest().build(); // Datos incompletos.
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Empleado> actualizar(@PathVariable(name = "id") Integer id, @RequestBody() Empleado nuevo) {

		try {
			return ResponseEntity.ok(empleadoServiceImpl.actualizarEmpleado(id, nuevo));
		} catch (EmpleadoNotFoundException e) {
			return ResponseEntity.badRequest().build(); //No existe empleado con ese id/
		} catch (EmpleadoForbiddenException e) {
			return ResponseEntity.badRequest().build(); //Id de la url y de "nuevo" distintos
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Empleado> borrar(@PathVariable(name = "id") Integer id) {
		
		try {
			return ResponseEntity.ok(empleadoServiceImpl.deleteEmpleadoById(id));
		} catch (EmpleadoNotFoundException e) {
			return ResponseEntity.badRequest().build(); //No existe empleado con ese id/
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}
