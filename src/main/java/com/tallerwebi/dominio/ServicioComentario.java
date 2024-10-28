package com.tallerwebi.dominio;

public interface ServicioComentario {
    void guardarComentario(Comentario comentario, Publicacion publicacion);
    void setearAutorYPublicacionEnUnComentario(Comentario comentario,Usuario autor, Publicacion publicacion);
}
