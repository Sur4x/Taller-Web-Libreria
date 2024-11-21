package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioNotificacion {

    void guardar(Notificacion notificacion);

    Notificacion buscarNotificacionPor(Long id);

    List<Notificacion> listarTodasLasNotificacionesDeUnUsuario(Long id);
}
