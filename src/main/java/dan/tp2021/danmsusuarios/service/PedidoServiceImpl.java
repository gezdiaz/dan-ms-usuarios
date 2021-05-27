package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.dto.PedidoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {
    @Override
    public List<PedidoDTO> getPedidoByClienteId(Integer id) {
        WebClient webClient = WebClient.create("http://localhost:9002/api/pedido?idCliente=" + id);
        ResponseEntity<List<PedidoDTO>> response = webClient.get().accept(MediaType.APPLICATION_JSON).retrieve()
                .toEntityList(PedidoDTO.class).block();
        if (response != null && response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        }
        return null;
    }
}
