package dan.tp2021.danmsusuarios.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.danmsusuarios.dao.EmpleadoRepository;
import dan.tp2021.danmsusuarios.domain.Empleado;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoNotFoundException;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

	private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

	@Autowired
	EmpleadoRepository empleadoRepository;

	@Override
	public List<Empleado> getEmpleadosByParams(String nombre) {
		if (nombre.isBlank()) {
			logger.trace("getEmpleadosByParams(): No se recibió nombre, retornando lista completa.");
			return getListaEmpleados();
		}
		List<Empleado> resultado = empleadoRepository.findByNombre(nombre);
		logger.debug("getEmpleadosByParams() Retornando empleados con nombre: \"" + nombre + "\": " + resultado);
		return resultado;
	}

	@Override
	public List<Empleado> getListaEmpleados() {
		List<Empleado> resultado = empleadoRepository.findAll();
		logger.debug("getListaEmpleados(): Retornando la lista de eplemados completa: " + resultado);
		return resultado;
	}

	@Override
	public Empleado getEmpleadoById(Integer id) throws EmpleadoNotFoundException {
		Optional<Empleado> e = empleadoRepository.findById(id);
		if (e.isPresent()) {
			logger.debug("getEmpleadoById(): Retornado el empleado: " + e.get());
			return e.get();
		}
		throw new EmpleadoNotFoundException("No se encontró empleado con id: " + id);
	}

	@Override
	public Empleado deleteEmpleadoById(Integer id) throws EmpleadoNotFoundException {
		Empleado e = getEmpleadoById(id);

		empleadoRepository.delete(e);
		logger.debug("deleteEmpleadoById(): Se eliminó el empleado: " + e);
		return e;
	
	}

	@Override
	public Empleado actualizarEmpleado(Integer id, Empleado e) throws EmpleadoNotFoundException, EmpleadoForbiddenException {
		if(id.equals(e.getId())) {
			if(empleadoRepository.existsById(id)) {
				return empleadoRepository.save(e);
			}
			throw new EmpleadoNotFoundException("No se encontro empleado con id: " + id);
		}
		logger.debug("actualizarEmpleado(): El id recibido no coincide con el empleado recibido. ID: '" + id + "', empleado: " + e);
		throw new EmpleadoForbiddenException("Los IDs deben coincidir");
	}

	@Override
	public Empleado saveEmpleado(Empleado e) {
		logger.debug("saveEmpleado(): Se guarda el empleado: " + e);
		return empleadoRepository.save(e);
	
	}

}
