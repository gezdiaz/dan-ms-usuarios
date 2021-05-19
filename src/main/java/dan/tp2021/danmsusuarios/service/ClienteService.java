package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException;

import java.util.List;


public interface ClienteService {

    Cliente saveCliente(Cliente c) throws ClienteException;
    Cliente darDeBaja(Integer idCLiente) throws ClienteException;
    List<Cliente> getListaClientes();
    Cliente getClienteById(Integer id) throws ClienteNotFoundException;
    List<Cliente> getClientesByParams(String rs) throws ClienteNotFoundException;
    Cliente getClienteByCuit(String cuit) throws ClienteNotFoundException;
    Cliente actualizarCliente(Integer id, Cliente c) throws ClienteException;

    
}
