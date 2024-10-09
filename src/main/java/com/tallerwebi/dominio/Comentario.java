package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "idPublicacion")
    private Publicacion publicacion;

    public Comentario(){}

    public Comentario(Long id, String texto, Usuario autor, Publicacion publicacion) {
        this.id = id;
        this.texto = texto;
        this.autor = autor;
        this.publicacion = publicacion;

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
}
