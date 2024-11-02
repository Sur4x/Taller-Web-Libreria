package com.tallerwebi.dominio;

public interface ServicioLike {

    Boolean agregarLike(Long comentarioId, Usuario autorDelLike);
    Integer obtenerCantidadDeLikesDeUnComentario(Long comentarioId);
}
