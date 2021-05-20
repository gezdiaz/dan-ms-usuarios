package dan.tp2021.danmsusuarios.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import dan.tp2021.danmsusuarios.domain.TipoObra;

public interface TipoObraRepository extends JpaRepository<TipoObra, Integer> {


    Optional<TipoObra> findByDescripcion(String descipcion);

    boolean existsByDescripcion(String descripcion);

}
