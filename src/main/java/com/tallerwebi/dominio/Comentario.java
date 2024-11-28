package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;
    private LocalDateTime fechaCreacion;

    @Transient
    private String fechaCreacionFormateada;

    @ManyToOne
    @JoinColumn()
    private Usuario autor; //bien

    @ManyToOne
    @JoinColumn()
    private Publicacion publicacion;

    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();

    public Comentario(){}

    public Comentario(Long id, String texto, Usuario autor, Publicacion publicacion, LocalDateTime fechaCreacion) {
        this.id = id;
        this.texto = texto;
        this.autor = autor;
        this.publicacion = publicacion;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public boolean tienelikeDeEsteUsuario(Usuario usuario) {
        for(Like like : this.likes) {
            if(like.getAutorDelLike().getId().equals(usuario.getId())) {
                return true;
            }
        }
        return false;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaCreacionFormateada() {
        return fechaCreacionFormateada;
    }

    public void setFechaCreacionFormateada(String fechaCreacionFormateada) {
        this.fechaCreacionFormateada = fechaCreacionFormateada;
    }
}
