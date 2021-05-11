package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.domain.Cliente;

import java.util.List;


public interface ClienteService {

    class ClienteException extends Exception { public ClienteException(String message){super(message);} }
    class ClienteNoHbilitadoException extends ClienteException { public ClienteNoHbilitadoException(String message){super(message);} }
    class ClienteNotFoundException extends ClienteException {public ClienteNotFoundException(String message){super(message);} }

    public Cliente saveCliente(Cliente c) throws ClienteException;
    public Cliente darDeBaja(Integer idCLiente) throws ClienteException;
    public List<Cliente> getListaClientes();
    public Cliente getClienteById(Integer id) throws ClienteNotFoundException;
    public List<Cliente> getClientesByParams(String rs) throws ClienteNotFoundException;
    public Cliente getClienteByCuit(String cuit) throws ClienteNotFoundException;
    public Cliente actualizarCliente(Integer id, Cliente c) throws ClienteException;
 
    
}
