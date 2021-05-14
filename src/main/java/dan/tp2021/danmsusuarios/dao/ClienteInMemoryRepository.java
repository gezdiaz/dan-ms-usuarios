package dan.tp2021.danmsusuarios.dao;

import dan.tp2021.danmsusuarios.domain.Cliente;
import frsf.isi.dan.InMemoryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Deprecated
@Repository
public class ClienteInMemoryRepository extends InMemoryRepository<Cliente> {
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
        List<Cliente> resultado = new ArrayList<>();
        super.findAll().forEach(resultado::add);
        return resultado.stream().filter(cliente -> cliente.getFechaBaja() == null || cliente.getFechaBaja().getTime() == 0).collect(Collectors.toList());
    }

    @Override
    public Optional<Cliente> findById(Integer id){
        Optional<Cliente> optionalCliente = this.findById(id);
        if(optionalCliente.isPresent() && optionalCliente.get().getFechaBaja() == null){
            return optionalCliente;
        }
        return null;
    }
}