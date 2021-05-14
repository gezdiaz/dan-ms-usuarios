package dan.tp2021.danmsusuarios.dao;

import org.springframework.stereotype.Repository;

import dan.tp2021.danmsusuarios.domain.Empleado;
import frsf.isi.dan.InMemoryRepository;

@Deprecated
@Repository
public class EmpleadoInMemoryRepository extends InMemoryRepository<Empleado> {

	@Override
	public Integer getId(Empleado entity) {
		
		return entity.getId();
	}

	@Override
	public void setId(Empleado entity, Integer id) {
		entity.setId(id);		
	}

}
