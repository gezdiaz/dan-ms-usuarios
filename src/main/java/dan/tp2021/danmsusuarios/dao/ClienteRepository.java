package dan.tp2021.danmsusuarios.dao;

import dan.tp2021.danmsusuarios.domain.Cliente;
import frsf.isi.dan.InMemoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClienteRepository extends InMemoryRepository<Cliente> {
    @Override
    public Integer getId(Cliente entity) {
        return entity.getId();
    }
    @Override
    public void setId(Cliente entity, Integer id) {
        entity.setId(id);
    }

    @Override
    public List<Cliente> findAll(){
        return this.findAll().stream().filter(cliente -> cliente.getFechaBaja().equals(null)).collect(Collectors.toList());
    }

    @Override
    public Optional<Cliente> findById(Integer id){
        Optional<Cliente> optionalCliente = this.findById(id);
        if(optionalCliente.get().getFechaBaja()==null){
            return optionalCliente;
        }
        return null;
    }
}