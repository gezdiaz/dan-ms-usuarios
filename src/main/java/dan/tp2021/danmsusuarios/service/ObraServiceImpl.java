package dan.tp2021.danmsusuarios.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.danmsusuarios.dao.ObraRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraNotFoundException;

@Service
public class ObraServiceImpl implements ObraService {

	private static final Logger logger = LoggerFactory.getLogger(ObraServiceImpl.class);

	@Autowired
	ObraRepository obraRepository;

	@Autowired
	ClienteService clienteServiceImpl;
	
	@Override
	public Obra getObraById(Integer id) throws ObraNotFoundException {
		Optional<Obra> o = obraRepository.findById(id);
		if (o.isPresent()) {
			logger.debug("getObraById(): Retornando obra: " + o.get());
			return o.get();
		}
		logger.debug("getObraById(): No se encontró la obra con id: " + id);
		throw new ObraNotFoundException("No existe obra con id: " + id);
	}

	@Override
	public List<Obra> getObraByParams(String tipoObra, Integer idCliente, String cuitCliente) {

		logger.debug("getObraByParams(): Se recibieron parámetros: tipoObra: '" + tipoObra + "' idCliente: '" + idCliente + "' y cuitCliente: '" + cuitCliente + "'");

		if(idCliente != null && idCliente <= 0) idCliente = null;
		if(tipoObra != null && tipoObra.isBlank()) tipoObra = null;
		if(cuitCliente != null && cuitCliente.isBlank()) cuitCliente = null;

		List<Obra> resultado = obraRepository.findByParams(tipoObra, idCliente, cuitCliente);

		logger.debug("getObraByParams(): retornando lista de obras: " + resultado);

		return resultado;
	}

	@Override
	public Obra deleteObraById(Integer id) throws ObraNotFoundException {

		Obra o = getObraById(id);
		logger.debug("deleteObraById(): Eliminando la obra: " + o);
		obraRepository.deleteById(id);
		return o;
	}

	@Override
	public Obra saveObra(Obra obra) throws ObraForbiddenException, ClienteException {

		if(obra.getCliente() == null){
			throw new ObraForbiddenException("No se pueden crear obras sin cliente.");
		}

		Cliente clienteCompleto = clienteServiceImpl.getClienteById(obra.getCliente().getId());
		//setear esta obra en el cliente
		logger.debug("saveObra(): Añadiendo la obra con id " + obra + " al cliente con id " + obra.getCliente().getId());
		clienteCompleto.getObras().add(obra);
		obra.setCliente(clienteCompleto);

		logger.debug("saveObra() Guardando obra: " + obra);
		Obra resultado = obraRepository.save(obra);
		logger.debug("saveObra() Guardando el cliente con la nueva obra: " + clienteCompleto);
		clienteServiceImpl.saveCliente(clienteCompleto);

		return resultado;
	}

	@Override
	public Obra actualizarObra(Integer id, Obra obra) throws ObraForbiddenException, ObraNotFoundException {

		if (obra.getId().equals(id)) {
			if (obraRepository.existsById(id)) {
				logger.debug("actualizarObra(): Actualizando obra: " + obra);
				return obraRepository.save(obra); // TODO ojo porque sobrescribe el objeto completo, los atributos vacios/nulos quedaran vacios/nulos en la BD
			}
			logger.debug("actualizarObra(): No se encontró la obra con id: " + id);
			throw new ObraNotFoundException("No se encontro obra con id: " + id);
		}
		logger.debug("actualizarObra(): Los ids no coinciden. id recibido: '" + id + "' id en la obra: '" + obra.getId() + "'");
		throw new ObraForbiddenException("Los IDs deben coincidir");

	}

}
