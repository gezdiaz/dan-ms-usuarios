package dan.tp2021.danmsusuarios.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import dan.tp2021.danmsusuarios.domain.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    List<Empleado> findByNombre(String nombre);

}
