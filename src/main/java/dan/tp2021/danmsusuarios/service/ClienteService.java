package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.domain.Cliente;

import java.util.List;


public interface ClienteService {

    class ClienteException extends Exception { ClienteException(String message){super(message);} }
    class ClienteNoHbilitadoException extends ClienteException { ClienteNoHbilitadoException(String message){super(message);} }
    class ClienteNotFoundException extends ClienteException { ClienteNotFoundException(String message){super(message);} }

    Cliente saveCliente(Cliente c) throws ClienteException;
    Cliente darDeBaja(Integer idCLiente) throws ClienteException;
    List<Cliente> getListaClientes();
}
