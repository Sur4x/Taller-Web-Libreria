package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motivo;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    public Reporte() {
    }

    public Reporte(Long id, String motivo, String descripcion, Club club) {
        this.id = id;
        this.motivo = motivo;
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