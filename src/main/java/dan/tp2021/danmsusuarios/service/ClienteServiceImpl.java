package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.dao.ClienteInMemoryRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.dto.PedidoDTO;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNoHabilitadoException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {
	@Autowired
	private BancoService bancoServiceImpl;

	@Autowired
	private ClienteInMemoryRepository clienteInMemoryRepository;

	@Autowired
	private PedidoService pedidoService;

	@Override
	public Cliente saveCliente(Cliente c) throws ClienteException {
		if (bancoServiceImpl.verificarRiesgo(c)) {
			//clienteRepository.save(c);
			//System.out.println(clienteRepository.findById(c.getId()).get().getRazonSocial());
			return clienteInMemoryRepository.save(c);
		}
		throw new ClienteNoHabilitadoException("Error. El cliente no cumple con los requisitos de riesgo.");
	}

	@Override
	public Cliente darDeBaja(Integer idCLiente) throws ClienteException{
		Optional<Cliente> c = clienteInMemoryRepository.findById(idCLiente);
		if (c.isPresent()) {
			List<PedidoDTO> pedidos;
			pedidos = pedidoService.getPedidoByClienteId(c.get().getId());
			if(pedidos!=null){
				if (!pedidos.isEmpty()) {
					c.get().setFechaBaja(new Date());
					return clienteInMemoryRepository.save(c.get());
				} else {
					clienteInMemoryRepository.delete(c.get());
					return c.get();
				}
			}else {
				throw new ClienteException("Error al obtener los pedidos desde el microservico de pedidos");
			}
		}
		throw new ClienteNotFoundException("Error al obtener el cliente");
	}

	@Override
	public List<Cliente> getListaClientes() {
		return  clienteInMemoryRepository.findAll();
	}

	@Override
	public Cliente getClienteById(Integer id) throws ClienteNotFoundException {
		Optional<Cliente> c = clienteInMemoryRepository.findById(id);
		if (c.isPresent()) {
			return c.get();
		}
		throw new ClienteNotFoundException("No existe cliente con id: " + id);

	}

	@Override
	public List<Cliente> getClientesByParams(String rs) {
		/*List<Cliente> resultado = getListaClientes();
		if (!rs.isBlank()) {
			resultado = resultado.stream().filter(p -> p.getRazonSocial().contains(rs)).collect(Collectors.toList());
			if (!resultado.isEmpty()) {
				return resultado;
			}
			throw new ClienteNotFoundException("No se encontraron clientes con razon social: " + rs);

		}
		if (!resultado.isEmpty()) {
			return resultado;
		}
		throw new ClienteNotFoundException("No se encontraron clientes.");*/
		List<Cliente> resultado = getListaClientes();
		if (!rs.isBlank()) {
			resultado = resultado.stream().filter(p -> p.getRazonSocial().contains(rs)).collect(Collectors.toList());
		}
		return resultado;
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

		if (c.getId().equals(id)) {
			if (clienteInMemoryRepository.existsById(id)) {
				clienteInMemoryRepository.save(c); // TODO ojo porque sobrescribe el objeto completo, los atributos vacios/nulos
											// quedaran vacios/nulos en la BD
			}
			throw new ClienteNotFoundException("No se encontro cliente con id: " + id);
		}

		throw new ClienteNoHabilitadoException("Los IDs deben coincidir");
	}
}
