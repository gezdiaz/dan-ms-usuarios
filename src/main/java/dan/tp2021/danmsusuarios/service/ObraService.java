package dan.tp2021.danmsusuarios.service;

import java.util.List;

import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.domain.TipoObra;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteException;
import dan.tp2021.danmsusuarios.exceptions.cliente.ClienteNotFoundException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraNotFoundException;
import dan.tp2021.danmsusuarios.exceptions.obra.TipoNoValidoException;

public interface ObraService {

	Obra getObraById(Integer id) throws ObraNotFoundException;
	List<Obra> getObraByParams(String tipoObra, Integer idCliente, String cuitCliente) throws ObraNotFoundException, ClienteNotFoundException;
	Obra deleteObraById(Integer id) throws ObraNotFoundException, TipoNoValidoException, ClienteException;
	Obra saveObra(Obra obra) throws ObraForbiddenException, ClienteException, TipoNoValidoException;
	Obra actualizarObra(Integer id, Obra obra) throws ObraException, TipoNoValidoException, ClienteException;

	//TODO Ver si no conviene generar un TipoObraService
	void validarTipo(Obra obra) throws TipoNoValidoException;
	void saveTipoObra(TipoObra tipo);
}
