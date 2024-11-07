package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
@Table(name = "club_puntuacion")
public class Puntuacion {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPuntuacion;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer puntuacion;

    public Puntuacion(){}

    public Puntuacion(Club club, Usuario usuario, Integer puntuacion) {
        this.club = club;
        this.usuario = usuario;
        this.puntuacion = puntuacion;
    }

    public Long getIdPuntuacion() {
        return idPuntuacion;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }
}