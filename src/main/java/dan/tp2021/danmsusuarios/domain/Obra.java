package dan.tp2021.danmsusuarios.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String descripcion;
    private Float latitud;
    private Float longitud;
    private String direccion;
    private Integer superficie;
    @ManyToOne
    private TipoObra tipo;
    @JsonBackReference
    @ManyToOne
    private Cliente cliente;
    //agrego también el id del cliente para no perder la referencia completamente,
    // ya que no puedo poner el cliente en json de la obre parque se genera una recursión infinita
    private Integer idCliente;

    public Obra() {
    }

    public Obra(Integer id, String descripcion, Float latitud, Float longitud, String direccion, Integer superficie, TipoObra tipo, Cliente cliente) {
        this.id = id;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.superficie = superficie;
        this.tipo = tipo;
        this.cliente = cliente;
        this.idCliente = cliente.getId();
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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getLatitud() {
        return latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Integer superficie) {
        this.superficie = superficie;
    }

    public TipoObra getTipo() {
        return tipo;
    }

    public void setTipo(TipoObra tipo) {
        this.tipo = tipo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.idCliente = cliente.getId();
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public void merge(Obra nuevo) {

        if (nuevo != null) {
            if (nuevo.getCliente() != null) {
                this.setCliente(nuevo.getCliente());
            }
            if (nuevo.getDescripcion() != null) {
                this.setDescripcion(nuevo.getDescripcion());
            }
            if (nuevo.getDireccion() != null) {
                this.setDireccion(nuevo.getDireccion());
            }
            if (nuevo.getLatitud() != null) {
                this.setLatitud(nuevo.getLatitud());
            }
            if (nuevo.getLongitud() != null) {
                this.setLongitud(nuevo.getLongitud());
            }
            if (nuevo.getSuperficie() != null) {
                this.setSuperficie(nuevo.getSuperficie());
            }
            if (nuevo.getTipo() != null) {
                this.setTipo(nuevo.getTipo());
            }
        }

    }
}
