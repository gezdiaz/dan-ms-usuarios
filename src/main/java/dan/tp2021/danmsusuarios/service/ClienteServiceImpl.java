package dan.tp2021.danmsusuarios.service;

import dan.tp2021.danmsusuarios.dao.ClienteRepository;
import dan.tp2021.danmsusuarios.dao.TipoObraRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.dto.PedidoDTO;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNoHabilitadoException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException;
import dan.tp2021.danmsusuarios.exceptions.obra.TipoNoValidoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
public class ClienteServiceImpl implements ClienteService {

	private static final Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BancoService bancoServiceImpl;

	@Autowired TipoObraRepository tipoObraRepository;

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private ObraService obraService;

	@Override
	public Cliente saveCliente(Cliente cliente) throws ClienteException, TipoNoValidoException {
		if (bancoServiceImpl.verificarRiesgo(cliente)) {
			// clienteRepository.save(c);
			// System.out.println(clienteRepository.findById(c.getId()).get().getRazonSocial());
			logger.debug("Cantidad de obras saveCleinte(): " + cliente.getObras().size());
			for (Obra obra: cliente.getObras()) {
				// Le seteo el cliente a todas las obras para guardarlas correctamente.
				// TODO probar guardar la obra aca y quitar el CascadeType.Persist. Porque
				// cuando cuandos e crea un cliente nuevo hay problemas al guardar un TipoObra
				// nuevo.
				obra.setCliente(cliente);

				obraService.validarTipo(obra);

				if(obra.getTipo().getId() == null) {
					//Si luego de validar el tipo de obra, el mismo sigue siendo null, es porque hay que crear uno nuevo.
					//Si no lo creamos aca, el cascade del saveCliente va a crear un nuevo tipo de obra por cada obra, con distintos IDs pero tal vez mismas descripciones,
					//y si se repiten las descripciones falla porque deben ser unicas.
					obraService.saveTipoObra(obra.getTipo());
				}

			}

			logger.debug("saveCliente(): Guardo el cliente: " + cliente);
			return clienteRepository.save(cliente);
		}
		logger.debug("saveCliente(): El cliente no fue autorizado por el banco: " + cliente);
		throw new ClienteNoHabilitadoException("Error. El cliente no cumple con los requisitos de riesgo.");
	}

	@Override
	public Cliente darDeBaja(Integer idCliente) throws ClienteException {
		Optional<Cliente> optionalCliente = clienteRepository.findById(idCliente);
		if (optionalCliente.isPresent()) {
			List<PedidoDTO> pedidos;
			pedidos = pedidoService.getPedidoByClienteId(optionalCliente.get().getId());
			if (pedidos != null) {
				if (!pedidos.isEmpty()) {

					optionalCliente.get().setFechaBaja(Instant.now());
					logger.debug("darDeBaja(): El cliente tiene pedidos, por lo tanto se le seta la fecha baja y se guardaÑ: " + optionalCliente.get());
					return clienteRepository.save(optionalCliente.get());
				} else {
					clienteRepository.deleteById(optionalCliente.get().getId());
					logger.debug("darDeBaja(): El cliente no tiene pedidos por lo que se eliminó: " + optionalCliente.get());
					return optionalCliente.get();
				}
			} else {
				logger.warn("darDeBaja(): Ocurrió un error al obtener los pedidos del cliente: " + optionalCliente.get());
				//throw new ClienteException("Error al obtener los pedidos desde el microservico de pedidos");
				//TODO? CIRCUIT BREAKER - Es mejor un cliente vacio o devolvemos el cliente sin eliminarlo?
				return optionalCliente.get();
			}
		}
		logger.warn("darDeBaja(): No se encontró al cliente con id: " + idCliente);
		throw new ClienteNotFoundException("Error al obtener el cliente");
	}

	@Override
	public List<Cliente> getListaClientes() {
		return clienteRepository.findByFechaBajaNullOrFechaBaja(Instant.EPOCH);
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
	public List<Cliente> getClientesByParams(String razonSocial) {
		List<Cliente> resultado;
		logger.debug("getClientesByParams(): buscando cliente por razon social: " + razonSocial);
		if (!razonSocial.isBlank()) {
			return clienteRepository.findByRazonSocial(razonSocial);
		}
		resultado = getListaClientes();
		return resultado;
	}

	@Override
	public Cliente getClienteByCuit(String cuit) throws ClienteNotFoundException {
		Optional<Cliente> resultado = clienteRepository.findByCuit(cuit);
		if (resultado.isPresent()) {
			return resultado.get();
		}
		throw new ClienteNotFoundException("No se encontro cliente con cuit: " + cuit);
	}

	@Override
	public Cliente actualizarCliente(Integer id, Cliente cliente) throws ClienteException, TipoNoValidoException {
		// TODO REVISAR, ver si recibimos un id o no, en los otros micorservicios no recibimos un id. Hay que ponernos de acuerdo.

		if (cliente.getId().equals(id)) {
			if (clienteRepository.existsById(id)) {
				return this.saveCliente(cliente); // TODO ojo porque sobrescribe el objeto completo, los atributos vacíos/nulos quedaran vacíos/nulos en la BD
			}
			throw new ClienteNotFoundException("No se encontró cliente con id: " + id);
		}

		throw new ClienteNoHabilitadoException("Los IDs deben coincidir");
	}
}
