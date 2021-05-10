package dan.tp2021.danmsusuarios.dao;

import org.springframework.stereotype.Repository;

import dan.tp2021.danmsusuarios.domain.Obra;
import frsf.isi.dan.InMemoryRepository;

@Repository
public class ObraRepository extends InMemoryRepository<Obra> {

	@Override
	public Integer getId(Obra entity) {
		return entity.getId();
	}

	@Override
	public void setId(Obra entity, Integer id) {
		entity.setId(id);
	}

}
