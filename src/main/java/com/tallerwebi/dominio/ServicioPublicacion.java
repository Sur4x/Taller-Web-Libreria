package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseClub;

public interface ServicioPublicacion {
    Publicacion buscarPublicacionPorId(Long idPublicacion);

    Publicacion buscarPublicacionEnUnClub(Long publicacionId, Club club);

    void agregarNuevaPublicacion(Publicacion publicacion, Long id) throws NoExisteEseClub;

    void eliminarPublicacion(Publicacion publicacion, Club club);
}
