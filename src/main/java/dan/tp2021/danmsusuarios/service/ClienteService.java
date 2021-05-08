package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.domain.Cliente;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ClienteService {
    public ResponseEntity<Cliente> saveCliente(Cliente c);
    public ResponseEntity<Cliente> darDeBaja(Integer idCLiente);
    public ResponseEntity<List<Cliente>> getListaClientes();
}
