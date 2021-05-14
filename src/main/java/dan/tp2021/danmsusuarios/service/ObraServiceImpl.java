package dan.tp2021.danmsusuarios.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.tp2021.danmsusuarios.dao.ObraInMemoryRepository;
import dan.tp2021.danmsusuarios.domain.Cliente;
import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraNotFoundException;

@Service
public class ObraServiceImpl implements ObraService {

	@Autowired
	ObraInMemoryRepository obraInMemoryRepository;

	@Autowired
	ClienteService clienteServiceImpl;
	
	@Override
	public Obra getObraById(Integer id) throws ObraNotFoundException {
		Optional<Obra> o = obraInMemoryRepository.findById(id);
		if (o.isPresent()) {
			return o.get();
		}
		throw new ObraNotFoundException("No existe obra con id: " + id);
	}

	@Override
	public List<Obra> getListaObras() throws ObraNotFoundException {
		List<Obra> resultado = new ArrayList<>();
		obraInMemoryRepository.findAll().forEach(o -> resultado.add(o));
		if (!resultado.isEmpty()) {
			return resultado;
		}
		throw new ObraNotFoundException("No se encontraron obras.");
	}

	@Override
	public List<Obra> getObraByParams(String tipoObra, Integer idCliente, String cuitCliente)
			throws ObraNotFoundException, ClienteNotFoundException {
		
		List<Obra> resultadoAux = new ArrayList<>();
		obraInMemoryRepository.findAll().forEach(o -> resultadoAux.add(o));
		List<Obra> resultado = resultadoAux;
		
		if (idCliente > 0) {
			resultado = resultado.stream().filter(o -> o.getIdCliente().equals(idCliente))
					.collect(Collectors.toList());
		}
		if (!tipoObra.isBlank()) {
			resultado = resultado.stream().filter(o -> o.getTipo().getDescriocion().contains(tipoObra))
					.collect(Collectors.toList());
		}

		if (!cuitCliente.isBlank()) {
			Cliente clienteBuscadoByCuit =  clienteServiceImpl.getClienteByCuit(cuitCliente);
			resultado = resultado.stream().filter(o -> clienteBuscadoByCuit.getId().equals(o.getIdCliente()))
					.collect(Collectors.toList());

		}
	
		if (!resultado.isEmpty()) {
			return resultado;
		}
		
		throw new ObraNotFoundException("No se encontraron obras.");
	}

	@Override
	public Obra deleteObraById(Integer id) throws ObraNotFoundException {

		Obra o = getObraById(id);
		obraInMemoryRepository.delete(o);
		return o;
	}

	@Override
	public Obra saveObra(Obra obra) {
		return obraInMemoryRepository.save(obra);
	}

	@Override
	public Obra actualizarObra(Integer id, Obra obra) throws ObraForbiddenException, ObraNotFoundException {

		if (obra.getId().equals(id)) {
			if (obraInMemoryRepository.existsById(id)) {
				return obraInMemoryRepository.save(obra); // TODO ojo porque sobrescribe el objeto completo, los atributos vacios/nulos
											// quedaran vacios/nulos en la BD
			}
			throw new ObraNotFoundException("No se encontro obra con id: " + id);
		}

		throw new ObraForbiddenException("Los IDs deben coincidir");

	}

}
