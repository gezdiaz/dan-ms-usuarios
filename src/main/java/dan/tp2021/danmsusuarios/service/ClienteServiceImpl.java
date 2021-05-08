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
    public ResponseEntity<Cliente> saveCliente(Cliente c) {
        if(bancoServiceImpl.verificarRiesgo(c)){
            clienteRepository.save(c);
            System.out.println(clienteRepository.findById(c.getId()).get().getRazonSocial());
            return ResponseEntity.ok(c);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Override
    public ResponseEntity<Cliente> darDeBaja(Integer idCLiente) {
        Optional<Cliente> c = clienteRepository.findById(idCLiente);
        if(c.isPresent()){
            List<PedidoDTO> pedidos = new ArrayList<>();
            WebClient webClient = WebClient.create("http://localhost:9011/api/pedido?idCliente="+c.get().getId());
            ResponseEntity<List<PedidoDTO>> response = webClient.get()
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntityList(PedidoDTO.class)
                    .block();
            if(response != null && response.getStatusCode() == HttpStatus.OK){
                pedidos = response.getBody();
                if(pedidos.size()>0){
                    c.get().setFechaBaja(new Date());
                    return ResponseEntity.ok(clienteRepository.save(c.get()));
                }else{
                    clienteRepository.delete(c.get());
                    return ResponseEntity.of(c);
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<Cliente>> getListaClientes() {
        List<Cliente> result = new ArrayList<>();
        Iterator<Cliente> it = clienteRepository.findAll().iterator();
        it.forEachRemaining(result::add);
        /*while (it.hasNext()){
            if(it.next().getFechaBaja()!=null){
            	System.out.println(it.next().getRazonSocial());
                result.add(it.next());
            }
        }*/
        return ResponseEntity.ok(result);
    }
}
