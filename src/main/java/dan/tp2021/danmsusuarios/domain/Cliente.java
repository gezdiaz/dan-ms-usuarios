package dan.tp2021.danmsusuarios.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.apache.catalina.User;

import java.util.Date;
import java.util.List;

public class Cliente {

    private Integer id;
    private String razonSocial;
    private String cuit;
    private String mail;
    private Double maxCuentaOnline;
    private Double saldoActual;
    //No hace falta el habilitado, cada vez que se necesite saber la situacion el sistema se comunicaria
    //con el sistema de BCRA
    private Boolean habilitadoOnline;
    @JsonManagedReference
    private List<Obra> obras;
    private Usuario user;
    private Date fechaBaja;

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
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

    public void merge(Cliente nuevo) {

        if (nuevo != null) {

            if(nuevo.getCuit() != null){
                this.setCuit(nuevo.getCuit());
            }

            if(nuevo.getRazonSocial() != null){
                this.setRazonSocial(nuevo.getRazonSocial());
            }

            if(nuevo.getHabilitadoOnline() != null){
                this.setHabilitadoOnline(nuevo.getHabilitadoOnline());
            }

            if(nuevo.getMail() != null){
                this.setMail(nuevo.getMail());
            }

            if(nuevo.getObras() != null){
                this.setObras(nuevo.getObras());
            }

            if(nuevo.getMaxCuentaOnline() != null){
                this.setMaxCuentaOnline(nuevo.getMaxCuentaOnline());
            }

        }

    }
}
