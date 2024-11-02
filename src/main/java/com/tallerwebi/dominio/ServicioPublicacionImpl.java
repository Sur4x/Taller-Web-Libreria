package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioPublicacionImpl implements ServicioPublicacion {

    private RepositorioPublicacion repositorioPublicacion;

    @Autowired
    public ServicioPublicacionImpl(RepositorioPublicacion repositorioPublicacion) {
        this.repositorioPublicacion = repositorioPublicacion;
    }

    @Override
    public Publicacion buscarPublicacionPorId(Long idPublicacion){
        Publicacion publicacion = repositorioPublicacion.buscarPublicacionPorId(idPublicacion);
        if (publicacion != null) {
            Hibernate.initialize(publicacion.getComentarios());
            for (Comentario comentario : publicacion.getComentarios()) {
                Hibernate.initialize(comentario.getLikes());
            }
        }
        return publicacion;
    }

    @Override
    public Publicacion buscarPublicacionEnUnClub(Long publicacionId, Club club) {
        return repositorioPublicacion.buscarPublicacionEnUnClub(publicacionId, club);
    }
}
