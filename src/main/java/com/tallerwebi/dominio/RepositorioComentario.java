package com.tallerwebi.dominio;

public interface RepositorioComentario {

    public void guardar(Comentario comentario);

    public Comentario buscarComentarioEnUnaPublicacion(Long comentarioId, Publicacion publicacion);

    void eliminarComentario(Comentario comentario);

    Comentario buscarComentarioPorId(Long comentarioId);

}
