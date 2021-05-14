package dan.tp2021.danmsusuarios.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import dan.tp2021.danmsusuarios.domain.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
}
