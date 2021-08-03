package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.config.PedidoRestProperties;
import dan.tp2021.danmsusuarios.dto.PedidoDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

	Logger logger = LoggerFactory.getLogger(PedidoServiceImpl.class);

	@Autowired
	PedidoRestProperties pedidoRestProperties;

	@Autowired
	private CircuitBreakerFactory circuitBreakerFactory;

	@Override
	public List<PedidoDTO> getPedidoByClienteId(Integer id) {
		String url = pedidoRestProperties.getUrl();
		logger.debug("Url de pedidos: " + url);

		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
		WebClient webClient = WebClient.create(url + "/api/pedido?idCliente=" + id);
		
		return circuitBreaker.run(() -> {
			ResponseEntity<List<PedidoDTO>> response = webClient.get().accept(MediaType.APPLICATION_JSON).retrieve()
					.toEntityList(PedidoDTO.class).block();
			logger.debug("Url de pedidos: " + url + "/api/pedido?idCliente=" + id);
			if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
				 return response.getBody();
			}
			return null;
		}, throwable -> defaultPedido());

	}

	public List<PedidoDTO> defaultPedido() {
		logger.debug("Entra a defaultPedido. Se abrio el circuito");
		return null;
	}
}
