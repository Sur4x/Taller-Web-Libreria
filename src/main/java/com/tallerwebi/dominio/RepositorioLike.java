package com.tallerwebi.dominio;

public interface RepositorioLike {

    Boolean validarSiUnUsuarioDioLikeAUnComentario(Long comentarioId, Long idAutorDelLike);

    void agregarLike(Like like);

    Integer obtenerCantidadDeLikesDeUnComentario(Long comentarioId);

    void eliminarLikesDeUnComentario(Long id);

    Boolean quitarLikeDeUnUsuario(Long comentarioId, Long id);
}
