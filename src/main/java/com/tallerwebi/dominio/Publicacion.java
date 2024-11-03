package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    private String titulo;

    private Long idClub;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club; //UN CLUB TIENE MUCHOS SUBTITULOS

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comentario> comentarios; //1 PUBLICACION TIENE MUCHOS COMENTARIOS

    public Publicacion(){
    }

    public Publicacion(Long id, Club club, String titulo, String mensaje, List<Comentario> comentarios) {
        this.id = id;
        this.club = club;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.comentarios = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public Long getIdClub() { return idClub; }

    public void setIdClub(Long idClub) { this.idClub = idClub; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setClub(Club club) { this.club = club;    }

    public Club getClub() {
        return club;
    }
}
