package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autorDelLike;

    @ManyToOne
    @JoinColumn(name = "comentario_id", nullable = false)
    private Comentario comentario;

    public Like(){

    }
    public Like(Long id, Usuario autorDelLike, Comentario comentario) {
        this.id = id;
        this.autorDelLike = autorDelLike;
        this.comentario = comentario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getAutorDelLike() {
        return autorDelLike;
    }

    public void setAutorDelLike(Usuario autorDelLike) {
        this.autorDelLike = autorDelLike;
    }

    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }
}
