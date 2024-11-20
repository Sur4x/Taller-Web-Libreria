package com.tallerwebi.dominio;

import java.util.List;
import java.util.Set;

public interface RepositorioPublicacion {

    Publicacion buscarPublicacionPorId(Long idPublicacion);
    void guardar(Publicacion publicacion);
    void eliminar(Publicacion publicacion);
    Publicacion buscarPublicacionEnUnClub(Long publicacionId, Club club);
    List<Publicacion> buscarPublicacionesMasRecientesDeUsuariosSeguidos(Set<Usuario> usuariosSeguidos);
}
