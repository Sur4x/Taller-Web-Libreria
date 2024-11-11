package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioNotificacion {

    void crearNotificacion(Notificacion notificacion);

    Notificacion buscarNotificacionPor(Long id);

    List<Notificacion> listarTodasLasNotificaciones();
}
