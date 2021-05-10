package dan.tp2021.danmsusuarios.service;

import java.util.List;

import dan.tp2021.danmsusuarios.domain.Obra;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraForbiddenException;
import dan.tp2021.danmsusuarios.exceptions.obra.ObraNotFoundException;

public interface ObraService {

	public Obra getObraById(Integer id) throws ObraNotFoundException;
	public List<Obra> getListaObras() throws ObraNotFoundException;
	public List<Obra> getObraByParams(String tipoObra, Integer idCliente, String cuitCliente) throws ObraNotFoundException;
	public Obra deleteObraById(Integer id) throws ObraNotFoundException;
	public Obra saveObra(Obra obra);
	public Obra actualizarObra(Integer id, Obra obra) throws ObraForbiddenException, ObraNotFoundException;
}
