package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteNingunaNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioNotificacionImpl implements ServicioNotificacion{

    @Autowired
    private RepositorioNotificacion repositorioNotificacion;

    @Override
    public void generarNotificacion(Notificacion notificacion) {
        repositorioNotificacion.crearNotificacion(notificacion);
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
    public List<Notificacion> obtenerElListadoDeNotificaciones() {
       List<Notificacion> notificaciones = repositorioNotificacion.listarTodasLasNotificaciones();
       return notificaciones != null ? notificaciones : new ArrayList<>();
    }
}
