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
public class ServicioNotificacionImpl implements ServicioNotificacion{


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
    public Notificacion buscarNotificacionPorId(Long id) throws NoExisteNingunaNotificacion{
        Notificacion notificacion = repositorioNotificacion.buscarNotificacionPor(id);

        if(notificacion == null){
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
        crearNotificacion(usuario, tipoNotificacion, nombreClub,null);
    }

    @Override
    public void crearNotificacion(Usuario usuario, String tipoNotificacion, String nombreClub, String mensajeReporte){
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDateTime.now());
        switch (tipoNotificacion){
            case "nuevoReporte":
                notificacion.setEvento("Admins, en su club" + nombreClub + "fue aprobada un reporte: " + mensajeReporte);
                break;
            case "clubEliminado":
                notificacion.setEvento("El club" + nombreClub + "fue eliminado de nuestra web, lo sentimos");
                break;
            case "usuarioEchado":
                notificacion.setEvento("Usted ha sido eliminado del club: " + nombreClub + " debido a una conducta inapropiada.");
                break;

            case "nuevoSeguidor":
                notificacion.setEvento("Usted posee un nuevo seguidor");
                break;
        }
        notificacion.setUsuario(usuario);
        usuario.getNotificaciones().add(notificacion);
        repositorioNotificacion.guardar(notificacion);
        repositorioUsuario.guardar(usuario);
    }

}
