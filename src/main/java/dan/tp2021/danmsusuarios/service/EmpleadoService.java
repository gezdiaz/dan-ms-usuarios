package dan.tp2021.danmsusuarios.service;

import java.util.List;

import dan.tp2021.danmsusuarios.domain.Empleado;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.empleado.EmpleadoNotFoundException;

public interface EmpleadoService {
	
	public List<Empleado> getEmpleadosByParams(String nombre) throws EmpleadoNotFoundException;
	public List<Empleado> getListaEmpleados();
	public Empleado getEmpleadoById(Integer id) throws EmpleadoNotFoundException;
	public Empleado deleteEmpleadoById(Integer id) throws EmpleadoNotFoundException;
	public Empleado actualizarEmpleado(Integer id, Empleado nuevo) throws EmpleadoNotFoundException, EmpleadoForbiddenException;
	public Empleado saveEmpleado(Empleado e);
}
