package com.tallerwebi.dominio;

public interface ServicioPublicacion {
    Publicacion buscarPublicacionPorId(Long idPublicacion);

    Publicacion buscarPublicacionEnUnClub(Long publicacionId, Club club);
}
