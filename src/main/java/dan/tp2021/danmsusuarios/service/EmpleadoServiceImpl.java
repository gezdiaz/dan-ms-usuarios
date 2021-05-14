package dan.tp2021.danmsusuarios.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.danmsusuarios.dao.EmpleadoInMemoryRepository;
import dan.tp2021.danmsusuarios.domain.Empleado;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoNotFoundException;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

	@Autowired
	EmpleadoInMemoryRepository empleadoInMemoryRepository;

	@Override
	public List<Empleado> getEmpleadosByParams(String nombre) throws EmpleadoNotFoundException {
		List<Empleado> resultadoAux = getListaEmpleados();
		if (nombre.isBlank()) {
			if (!resultadoAux.isEmpty()) {
				return resultadoAux;
			}
			throw new EmpleadoNotFoundException("No se encontraron empleados.");
		}
		List<Empleado> resultado = resultadoAux.stream().filter(e -> e.getNombre().contains(nombre))
				.collect(Collectors.toList());
		if (!resultado.isEmpty()) {
			return resultado;
		}
		throw new EmpleadoNotFoundException("No se encontraron empelados que coincidan con: " + nombre);
	}

	@Override
	public List<Empleado> getListaEmpleados() {
		List<Empleado> resultadoAux = new ArrayList<>();
		empleadoInMemoryRepository.findAll().forEach(e -> resultadoAux.add(e));
		return resultadoAux;
	}

	@Override
	public Empleado getEmpleadoById(Integer id) throws EmpleadoNotFoundException {
		Optional<Empleado> e = empleadoInMemoryRepository.findById(id);
		if (e.isPresent()) {
			return e.get();
		}
		throw new EmpleadoNotFoundException("No se encontro empleado con id: " + id);
	}

	@Override
	public Empleado deleteEmpleadoById(Integer id) throws EmpleadoNotFoundException {
		Empleado e = getEmpleadoById(id);

		empleadoInMemoryRepository.delete(e);
		
		return e;
	
	}

	@Override
	public Empleado actualizarEmpleado(Integer id, Empleado e) throws EmpleadoNotFoundException, EmpleadoForbiddenException {
		if(id.equals(e.getId())) {
			if(empleadoInMemoryRepository.existsById(id)) {
				return empleadoInMemoryRepository.save(e);
			}
			throw new EmpleadoNotFoundException("No se encontro empleado con id: " + id);
		}
		
		throw new EmpleadoForbiddenException("Los IDs deben coincidir");
	}

	@Override
	public Empleado saveEmpleado(Empleado e) {
		
		return empleadoInMemoryRepository.save(e);
	
	}

}
