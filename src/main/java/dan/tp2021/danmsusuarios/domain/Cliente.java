package dan.tp2021.danmsusuarios.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Cliente.class)
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String razonSocial;
    private String cuit;
    private String mail;
    private Double maxCuentaOnline;
    private Double saldoActual;
    //No hace falta el habilitado, cada vez que se necesite saber la situacion el sistema se comunicaria
    //con el sistema de BCRA
    private Boolean habilitadoOnline;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Obra> obras;
    @OneToOne(cascade = CascadeType.ALL)
    private Usuario user;
    private Instant fechaBaja;

    public Instant getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Instant fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Cliente() {
    }

    public Cliente(Integer id, String razonSocial, String cuit, String mail, Double maxCuentaOnline, Boolean habilitadoOnline, List<Obra> obras, Usuario user) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.mail = mail;
        this.maxCuentaOnline = maxCuentaOnline;
        this.habilitadoOnline = habilitadoOnline;
        this.obras = obras;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Double getMaxCuentaOnline() {
        return maxCuentaOnline;
    }

    public void setMaxCuentaOnline(Double maxCuentaOnline) {
        this.maxCuentaOnline = maxCuentaOnline;
    }

    public Double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(Double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public Boolean getHabilitadoOnline() {
        return habilitadoOnline;
    }

    public void setHabilitadoOnline(Boolean habilitadoOnline) {
        this.habilitadoOnline = habilitadoOnline;
    }

    public List<Obra> getObras() {
        return obras;
    }

    public void setObras(List<Obra> obras) {
        this.obras = obras;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", razonSocial='" + razonSocial + '\'' +
                ", cuit='" + cuit + '\'' +
                ", mail='" + mail + '\'' +
                ", maxCuentaOnline=" + maxCuentaOnline +
                ", saldoActual=" + saldoActual +
                ", habilitadoOnline=" + habilitadoOnline +
                ", Id obras=" + (obras != null ? obras.stream().map(Obra::getId) : "null") +
                ", user=" + user +
                ", fechaBaja=" + fechaBaja +
                '}';
    }
}
