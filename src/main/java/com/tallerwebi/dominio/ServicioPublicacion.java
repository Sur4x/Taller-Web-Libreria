package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseClub;

import java.util.List;
import java.util.Set;

public interface ServicioPublicacion {
    Publicacion buscarPublicacionPorId(Long idPublicacion);

    Publicacion buscarPublicacionEnUnClub(Long publicacionId, Club club);

    void agregarNuevaPublicacion(Publicacion publicacion, Long id, Usuario usuario) throws NoExisteEseClub;

    void eliminarPublicacion(Publicacion publicacion, Club club);

    List<Publicacion> obtenerPublicacionesDeUsuariosSeguidos(Set<Usuario> usuariosSeguidos);
}
