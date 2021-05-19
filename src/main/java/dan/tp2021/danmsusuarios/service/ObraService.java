package dan.tp2021.danmsusuarios.service;

import java.util.List;

import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraNotFoundException;

public interface ObraService {

	Obra getObraById(Integer id) throws ObraNotFoundException;
	List<Obra> getObraByParams(String tipoObra, Integer idCliente, String cuitCliente) throws ObraNotFoundException, ClienteNotFoundException;
	Obra deleteObraById(Integer id) throws ObraNotFoundException;
	Obra saveObra(Obra obra) throws ObraForbiddenException, ClienteNotFoundException, ClienteException;
	Obra actualizarObra(Integer id, Obra obra) throws ObraForbiddenException, ObraNotFoundException;
}
