package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException;

import java.util.List;


public interface ClienteService {

    public Cliente saveCliente(Cliente c) throws ClienteException;
    public Cliente darDeBaja(Integer idCLiente) throws ClienteException;
    public List<Cliente> getListaClientes();
    public Cliente getClienteById(Integer id) throws ClienteNotFoundException;
    public List<Cliente> getClientesByParams(String rs) throws ClienteNotFoundException;
    public Cliente getClienteByCuit(String cuit) throws ClienteNotFoundException;
    public Cliente actualizarCliente(Integer id, Cliente c) throws ClienteException;
 
    
}
