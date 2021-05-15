package dan.tp2021.danmsusuarios.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dan.tp2021.danmsusuarios.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByRazonSocial(String razonSocial);

    Optional<Cliente> findByCuit(String cuit);

    List<Cliente> findByFechaBajaNullOrFechaBaja(LocalDate fechaBaja);

    Optional<Cliente> findByFechaBajaNullAndId(Integer id);

}
