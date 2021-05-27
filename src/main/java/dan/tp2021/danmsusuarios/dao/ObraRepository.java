package dan.tp2021.danmsusuarios.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import dan.tp2021.danmsusuarios.domain.Obra;

public interface ObraRepository extends JpaRepository<Obra, Integer> {

    @Query("SELECT o FROM Obra o WHERE (:tipoObra IS NULL OR o.tipo.descripcion like %:tipoObra%) AND (:idCliente IS NULL OR o.cliente.id = :idCliente ) AND (:cuitCliente IS NULL OR o.cliente.cuit = :cuitCliente)")
    List<Obra> findByParams(String tipoObra, Integer idCliente, String cuitCliente);

}
