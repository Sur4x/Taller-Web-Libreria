package com.tallerwebi.dominio;

public interface ServicioLike {

    Boolean agregarLike(Long comentarioId, Usuario autorDelLike);

    //borrar
    Integer obtenerCantidadDeLikesDeUnComentario(Long comentarioId);
}
