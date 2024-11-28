package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteNingunaNotificacion;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioNotificacionImpl implements ServicioNotificacion {


    private RepositorioNotificacion repositorioNotificacion;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioNotificacionImpl(RepositorioNotificacion repositorioNotificacion, RepositorioUsuario repositorioUsuario) {
        this.repositorioNotificacion = repositorioNotificacion;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public void generarNotificacion(Notificacion notificacion) {
        repositorioNotificacion.guardar(notificacion);
    }

    @Override
    public Notificacion buscarNotificacionPorId(Long id) throws NoExisteNingunaNotificacion {
        Notificacion notificacion = repositorioNotificacion.buscarNotificacionPor(id);

        if (notificacion == null) {
            throw new NoExisteNingunaNotificacion("No hay notificacion registrada con ese id");
        }
        return notificacion;
    }

    @Override
    public List<Notificacion> obtenerElListadoDeNotificacionesDeUnUsuario(Usuario usuario) {
        List<Notificacion> notificaciones = repositorioNotificacion.listarTodasLasNotificacionesDeUnUsuario(usuario.getId());
        return notificaciones != null ? notificaciones : new ArrayList<>();
    }

    @Override
    public void crearNotificacion(Usuario usuario, String tipoNotificacion, String nombreClub) {
        crearNotificacion(usuario, tipoNotificacion, nombreClub, null);
    }

    @Override
    public void crearNotificacion(Usuario usuario, String tipoNotificacion, String nombreClub, String mensajeReporte) {
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDateTime.now());
        switch (tipoNotificacion) {
            case "nuevoReporte":
                notificacion.setEvento("El reporte en el club " + nombreClub + " fue aprobado" + mensajeReporte);
                break;
            case "clubEliminado":
                notificacion.setEvento("El club " + nombreClub + " fue eliminado de nuestra web.");
                break;
            case "usuarioEchado":
                notificacion.setEvento("Usted ha sido eliminado del club: " + nombreClub);
                break;

            case "nuevoSeguidor":
                notificacion.setEvento("Un usuario acaba de seguirte.");
                break;
        }
        notificacion.setUsuario(usuario);
        usuario.getNotificaciones().add(notificacion);
        repositorioNotificacion.guardar(notificacion);
        repositorioUsuario.guardar(usuario);
    }

    @Override
    public void enviarNotificacionDeReporteAprobadoALosAdminDelClub(Long idClub, Reporte reporte) {

    }

    @Override
    public void crearNotificacionReporteAprobado(Club club, Reporte reporte) {

        List<Usuario> adminsSecundarios = club.getAdminsSecundarios();
        Usuario adminPrincipalDeClub = club.getAdminPrincipal();

        List<Usuario> usuariosNotificados = new ArrayList<>();
        usuariosNotificados.add(adminPrincipalDeClub);  // Agregar el admin principal
        usuariosNotificados.addAll(adminsSecundarios);  // Agregar los administradores secundarios

        // Recorrer la lista de usuarios y enviarles la notificación
        for (Usuario usuario : usuariosNotificados) {
            Notificacion notificacion = new Notificacion();
            notificacion.setFecha(LocalDateTime.now());
            notificacion.setEvento("En su club con el nombre:  " + club.getNombre() + "<br>" +  " fue aprobado un reporte, decidimos que hay suficientes pruebas para que ustedes tomen medidas concretas: " + "<br>" +  reporte.getMotivo());

            notificacion.setUsuario(usuario);
            usuario.getNotificaciones().add(notificacion);  // Agregar la notificación al usuario
            repositorioNotificacion.guardar(notificacion);   // Guardar la notificación en la base de datos
            repositorioUsuario.guardar(usuario);             // Guardar los cambios en el usuario
        }
    }

}
