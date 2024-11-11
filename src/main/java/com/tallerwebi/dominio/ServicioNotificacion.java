package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteNingunaNotificacion;

import java.util.List;

public interface ServicioNotificacion {

    void generarNotificacion(Notificacion notificacion);

    Notificacion buscarNotificacionPorId(Long id) throws NoExisteNingunaNotificacion;

    List<Notificacion> obtenerElListadoDeNotificaciones();
}
