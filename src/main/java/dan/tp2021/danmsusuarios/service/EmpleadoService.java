package dan.tp2021.danmsusuarios.service;

import java.util.List;

import dan.tp2021.danmsusuarios.domain.Empleado;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoNotFoundException;

public interface EmpleadoService {
	
	List<Empleado> getEmpleadosByParams(String nombre) throws EmpleadoNotFoundException;
	List<Empleado> getListaEmpleados();
	Empleado getEmpleadoById(Integer id) throws EmpleadoNotFoundException;
	Empleado deleteEmpleadoById(Integer id) throws EmpleadoNotFoundException;
	Empleado actualizarEmpleado(Integer id, Empleado nuevo) throws EmpleadoNotFoundException, EmpleadoForbiddenException;
	Empleado saveEmpleado(Empleado e);
}
