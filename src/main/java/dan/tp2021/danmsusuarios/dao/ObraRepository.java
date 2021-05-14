package dan.tp2021.danmsusuarios.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import dan.tp2021.danmsusuarios.domain.Obra;

public interface ObraRepository extends JpaRepository<Obra, Integer> {
}
