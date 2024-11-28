package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id") // Relación con Usuario
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    private String motivo;
    private String descripcion;
    private String estado;

    public Reporte() {
        estado = "pendiente";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Reporte(Long id, String motivo, String descripcion, Club club) {
        this.id = id;
        this.motivo = motivo;
        this.estado = "pendiente";
        this.descripcion = descripcion;
        this.club = club;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }



}