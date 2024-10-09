package com.tallerwebi.dominio;

public interface RepositorioPublicacion {

    Publicacion buscarPublicacionPorId(Long idPublicacion);

    void guardar(Publicacion publicacion);
    void eliminar(Publicacion publicacion);
}
