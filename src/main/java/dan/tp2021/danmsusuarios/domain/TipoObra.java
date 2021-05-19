package dan.tp2021.danmsusuarios.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TipoObra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descripcion;

    public TipoObra() {
    }

    public TipoObra(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descriocion) {
        this.descripcion = descriocion;
    }
}
