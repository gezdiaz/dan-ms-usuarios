package dan.tp2021.danmsusuarios.domain;

public class TipoObra {

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
