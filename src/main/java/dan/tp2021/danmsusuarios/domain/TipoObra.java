package dan.tp2021.danmsusuarios.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TipoObra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String descriocion;

    public TipoObra() {
    }

    public TipoObra(Integer id, String descriocion) {
        this.id = id;
        this.descriocion = descriocion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescriocion() {
        return descriocion;
    }

    public void setDescriocion(String descriocion) {
        this.descriocion = descriocion;
    }
}
