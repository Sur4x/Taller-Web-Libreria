package com.tallerwebi.dominio;

public interface RepositorioPublicacion {

    Publicacion buscarPublicacionPorId(Long idPublicacion);
    void guardar(Publicacion publicacion);
    void eliminar(Publicacion publicacion);
    Publicacion buscarPublicacionEnUnClub(Long publicacionId, Club club);
}
