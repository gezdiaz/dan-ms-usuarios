package dan.tp2021.danmsusuarios.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Empleado {

    @Id
    private Integer id;
    private String email;
    @OneToOne
    private Usuario user;
    //Agregado porque pide buscar por nombre, pero no está en el diagrama.
    private String nombre;

    public Empleado() {
    }

    public Empleado(Integer id, String email, Usuario user, String nombre) {
        this.id = id;
        this.email = email;
        this.user = user;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void merge(Empleado nuevo){

        if (nuevo != null) {
            if (nuevo.getEmail() != null) {
                this.setEmail(nuevo.getEmail());
            }
            if(nuevo.getUser() != null){
                this.setUser(nuevo.getUser());
            }
        }

    }
}
