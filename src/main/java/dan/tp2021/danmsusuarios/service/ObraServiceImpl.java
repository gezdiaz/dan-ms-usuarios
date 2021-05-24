package dan.tp2021.danmsusuarios.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.danmsusuarios.dao.ObraRepository;
import dan.tp2021.danmsusuarios.dao.TipoObraRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.domain.TipoObra;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraNotFoundException;
import dan.tp2021.danmsusuarios.exceptions.obra.TipoNoValidoException;

@Service
public class ObraServiceImpl implements ObraService {

	private static final Logger logger = LoggerFactory.getLogger(ObraServiceImpl.class);

	@Autowired
	ObraRepository obraRepository;

	@Autowired
	ClienteService clienteServiceImpl;

	@Autowired
	TipoObraRepository tipoObraRepository;
	
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
	public Obra deleteObraById(Integer id) throws ObraNotFoundException, TipoNoValidoException, ClienteException {

		Obra o = getObraById(id);
		//elimino la obra del cliente primero, para romper la relación y poder eliminar la obra, sino sa error.
		o.getCliente().getObras().remove(o);
		clienteServiceImpl.saveCliente(o.getCliente());
		logger.debug("deleteObraById(): Eliminando la obra: " + o);
		obraRepository.deleteById(id);
		return o;
	}

	@Override
	public Obra saveObra(Obra obra) throws ObraForbiddenException, ClienteException, TipoNoValidoException {

		if(obra.getCliente() == null || obra.getTipo() == null){
			throw new ObraForbiddenException("No se pueden crear obras sin cliente o tipo.");
		}

		Cliente clienteCompleto = clienteServiceImpl.getClienteById(obra.getCliente().getId());
		//setear esta obra en el cliente
		logger.debug("saveObra(): Añadiendo la obra con id " + obra + " al cliente con id " + obra.getCliente().getId());
		clienteCompleto.getObras().add(obra);
		obra.setCliente(clienteCompleto);

		validarTipo(obra);

		logger.debug("saveObra() Guardando obra: " + obra);
		Obra resultado = obraRepository.save(obra);
		logger.debug("saveObra() Guardando el cliente con la nueva obra: " + clienteCompleto);
		clienteServiceImpl.saveCliente(clienteCompleto);

		return resultado;
	}

	@Override
	public void validarTipo(Obra obra) throws TipoNoValidoException {
		TipoObra tipoRecibido = obra.getTipo();
		boolean tipoValidado = false;

		logger.debug("validarTipo() Se recibió el tipo: " + tipoRecibido);

		if(tipoRecibido.getId() != null && tipoRecibido.getId() > 0){
			//hay un tipo de obra con el mismo id que el recibido.
			logger.trace("validarTipo(): El tipo tiene id: " + tipoRecibido.getId());
			Optional<TipoObra> tipoBD = tipoObraRepository.findById(tipoRecibido.getId());
			if(tipoBD.isPresent()){
				//Existe un tipo con este id
				logger.trace("validarTipo(): El tipo con ese id existe en la base de datos");
				if(tipoRecibido.getDescripcion() != null && !tipoBD.get().getDescripcion().equals(tipoRecibido.getDescripcion())){
					//Error los tipos tienen el mismo id pero distintas descripciones, esto no está permitido.
					logger.debug("validarTipo(): La descripción del tipo en la base de datos no coincide con la descripción recibida. Descripción almacenada: \"" + tipoBD.get().getDescripcion() + "\" descipción recibida: \"" + tipoRecibido.getDescripcion() + "\"");
					throw new TipoNoValidoException("El tipo de obra recibido tiene un id válido existente con una descripción distinta a la recibida");
				}
				//En este punto el tipo está validado y mergeado con la BD, no va a dar detachend entity.
				logger.debug("validarTipo(): Se recibió un tipo con id válido y se va a setear ese tipo en la obra. El tipo: " + tipoBD.get());
				obra.setTipo(tipoBD.get());
				tipoValidado = true;
			}
		}
		if(!tipoValidado){
			logger.debug("validarTipo(): El tipo no tiene id o tiene un id inexistente.");
			//No se encontró el tipo por id o no tiene id, si la descripción no es válida es un error, porque hay que crear un nuevo tipo con esa descripción si no existe.
			if(tipoRecibido.getDescripcion() == null || tipoRecibido.getDescripcion().isBlank()){
				//Error la descipción no es válida no se puede crear un tipo así.
				logger.debug("validarTipo(): No se recibió descripción para el tipo sin id.");
				throw new TipoNoValidoException("Se recibió un tipo sin id o con un id inexistente pero con una descripción inválida.");
			}
			logger.trace("validarTipo(): Se recibió la descripción: \"" + tipoRecibido.getDescripcion() + "\". Buscando tipo en la base de datos");
			Optional<TipoObra> tipoByDescipcion = tipoObraRepository.findByDescripcion(tipoRecibido.getDescripcion());
			//Ya existe un tipo con esta descripción, se lo seteo a la obra para que no se cree otro.
			if(tipoByDescipcion.isPresent()){
				logger.debug("validarTipo(): Se encontró un tipo con la descripción recibida: " + tipoByDescipcion.get());
				obra.setTipo(tipoByDescipcion.get());
			} else {
				//Si el tipo no existe seteo el id en null para asegurar que se cree
				logger.debug("validarTipo(): No se encontró un tipo con la descripción \"" + tipoRecibido.getDescripcion() + "\". Seteando el id en null para crear el nuevo tipo.");
				obra.getTipo().setId(null);
			}
		}
	}

	@Override
	public Obra actualizarObra(Integer id, Obra obra) throws ObraForbiddenException, ObraNotFoundException, TipoNoValidoException {

		if (obra.getId().equals(id)) {
			if (obraRepository.existsById(id)) {
				logger.debug("actualizarObra(): Actualizando obra: " + obra);
				validarTipo(obra);
				return obraRepository.save(obra); // TODO ojo porque sobrescribe el objeto completo, los atributos vacios/nulos quedaran vacios/nulos en la BD
			}
			logger.debug("actualizarObra(): No se encontró la obra con id: " + id);
			throw new ObraNotFoundException("No se encontro obra con id: " + id);
		}
		logger.debug("actualizarObra(): Los ids no coinciden. id recibido: '" + id + "' id en la obra: '" + obra.getId() + "'");
		throw new ObraForbiddenException("Los IDs deben coincidir");

	}

}
