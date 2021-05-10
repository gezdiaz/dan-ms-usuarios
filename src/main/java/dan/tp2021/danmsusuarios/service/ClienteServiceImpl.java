package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.dao.ClienteRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.dto.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class ClienteServiceImpl implements ClienteService{
    @Autowired
    private BancoService bancoServiceImpl;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente saveCliente(Cliente c) throws ClienteException {
        if(bancoServiceImpl.verificarRiesgo(c)){
            clienteRepository.save(c);
            System.out.println(clienteRepository.findById(c.getId()).get().getRazonSocial());
            return c;
        }
        throw new ClienteNoHbilitadoException("Error. El cliente no cumple con los requisitos de riesgo.");
    }

    @Override
    public Cliente darDeBaja(Integer idCLiente) throws ClienteException {
        Optional<Cliente> c = clienteRepository.findById(idCLiente);
        if(c.isPresent()){
            List<PedidoDTO> pedidos;
            WebClient webClient = WebClient.create("http://localhost:9011/api/pedido?idCliente="+c.get().getId());
            ResponseEntity<List<PedidoDTO>> response = webClient.get()
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntityList(PedidoDTO.class)
                    .block();
            if(response != null && response.getStatusCode().equals(HttpStatus.OK)){
                pedidos = response.getBody();
                if(pedidos.size()>0){
                    c.get().setFechaBaja(new Date());
                    return clienteRepository.save(c.get());
                }else{
                    clienteRepository.delete(c.get());
                    return c.get();
                }
            }
            //TODO acá podríamos ver que error nos devolvió la API de pedidos y lanzar distintas excepciones según eso.
            throw new ClienteException("Error al obtener los pedidos desde el microservico de pedidos");
        }
        throw new ClienteNotFoundException("");
    }

    @Override
    public List<Cliente> getListaClientes() {
        List<Cliente> result = new ArrayList<>();
        Iterator<Cliente> it = clienteRepository.findAll().iterator();
        it.forEachRemaining(result::add);
        /*while (it.hasNext()){
            if(it.next().getFechaBaja()!=null){
            	System.out.println(it.next().getRazonSocial());
                result.add(it.next());
            }
        }*/
        return result;
    }
}
