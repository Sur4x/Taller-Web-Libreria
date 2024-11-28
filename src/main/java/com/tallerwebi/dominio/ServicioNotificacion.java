package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteNingunaNotificacion;

import java.util.List;

public interface ServicioNotificacion {

    void generarNotificacion(Notificacion notificacion);

    Notificacion buscarNotificacionPorId(Long id) throws NoExisteNingunaNotificacion;

    List<Notificacion> obtenerElListadoDeNotificacionesDeUnUsuario(Usuario usuario);

    void crearNotificacion(Usuario usuario, String tipoNotificacion, String nombreClub);

    void crearNotificacion(Usuario usuario, String tipoNotificacion, String nombreClub, String mensajeReporte);

    void enviarNotificacionDeReporteAprobadoALosAdminDelClub(Long idClub, Reporte reporte);

    void crearNotificacionReporteAprobado(Club club, Reporte reporte);
}
