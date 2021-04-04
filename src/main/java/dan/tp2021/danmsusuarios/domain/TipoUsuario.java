package dan.tp2021.danmsusuarios.domain;

public class TipoUsuario {

    private Integer id;
    private String  tipo;

    public TipoUsuario() {
    }

    public TipoUsuario(Integer id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
