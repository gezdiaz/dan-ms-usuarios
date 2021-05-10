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
public class ClienteServiceImpl implements ClienteService {
	@Autowired
	private BancoService bancoServiceImpl;

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public Cliente saveCliente(Cliente c) throws ClienteException {
		if (bancoServiceImpl.verificarRiesgo(c)) {
			clienteRepository.save(c);
			System.out.println(clienteRepository.findById(c.getId()).get().getRazonSocial());
			return c;
		}
		throw new ClienteNoHbilitadoException("Error. El cliente no cumple con los requisitos de riesgo.");
	}

	@Override
	public Cliente darDeBaja(Integer idCLiente) throws ClienteException {
		Optional<Cliente> c = clienteRepository.findById(idCLiente);
		if (c.isPresent()) {
			List<PedidoDTO> pedidos;
			WebClient webClient = WebClient.create("http://localhost:9011/api/pedido?idCliente=" + c.get().getId());
			ResponseEntity<List<PedidoDTO>> response = webClient.get().accept(MediaType.APPLICATION_JSON).retrieve()
					.toEntityList(PedidoDTO.class).block();
			if (response != null && response.getStatusCode().equals(HttpStatus.OK)) {
				pedidos = response.getBody();
				if (pedidos.size() > 0) {
					c.get().setFechaBaja(new Date());
					return clienteRepository.save(c.get());
				} else {
					clienteRepository.delete(c.get());
					return c.get();
				}
			}
			// TODO acá podríamos ver que error nos devolvió la API de pedidos y lanzar
			// distintas excepciones según eso.
			throw new ClienteException("Error al obtener los pedidos desde el microservico de pedidos");
		}
		throw new ClienteNotFoundException("");
	}

	@Override
	public List<Cliente> getListaClientes() {
		List<Cliente> result = new ArrayList<>();
		Iterator<Cliente> it = clienteRepository.findAll().iterator();
		it.forEachRemaining(result::add);

		return result;
	}

	@Override
	public Cliente getClienteById(Integer id) throws ClienteNotFoundException {
		Optional<Cliente> c = clienteRepository.findById(id);
		if (c.isPresent()) {
			return c.get();
		}
		throw new ClienteNotFoundException("No existe cliente con id: " + id);

	}

	@Override
	public List<Cliente> getClientesByParams(String rs) throws ClienteNotFoundException {
		List<Cliente> resultado = getListaClientes();
		if (!rs.isBlank()) {
			resultado.stream().filter(p -> p.getRazonSocial().contains(rs));
			if (!resultado.isEmpty()) {
				return resultado;
			}
			throw new ClienteNotFoundException("No se encontraron clientes con razon social: " + rs);
			
		}
		if(!resultado.isEmpty()) {
			return resultado;
		}
		throw new ClienteNotFoundException("No se encontraron clientes."); 
	}

	@Override
	public Cliente getClienteByCuit(String cuit) throws ClienteNotFoundException {
		Optional<Cliente> resultado = getListaClientes().stream().filter(c -> c.getCuit().equals(cuit)).findFirst();
		if (resultado.isPresent()) {
			return resultado.get();
		}
		throw new ClienteNotFoundException("No se encontro cliente con cuit: " + cuit);
	}

	@Override
	public Cliente actualizarCliente(Integer id, Cliente c) throws ClienteException {
		
		if(c.getId().equals(id)) {
			if(clienteRepository.existsById(id)) {
				clienteRepository.save(c); //TODO ojo porque sobrescribe el objeto completo, los atributos vacios/nulos quedaran vacios/nulos en la BD
			}
			throw new ClienteNotFoundException("No se encontro cliente con id: " + id);
		}
		
		throw new ClienteNoHbilitadoException("Los IDs deben coincidir");
	}
}
