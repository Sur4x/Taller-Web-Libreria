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
    private RepositorioClub repositorioClub;

    @Autowired
    public ServicioPublicacionImpl(RepositorioPublicacion repositorioPublicacion,RepositorioClub repositorioClub) {
        this.repositorioPublicacion = repositorioPublicacion;
        this.repositorioClub = repositorioClub;
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

    @Override
    public void agregarNuevaPublicacion(Publicacion publicacion, Long id) throws NoExisteEseClub {

        if (publicacion != null && id != null){
            Club club = repositorioClub.buscarClubPor(id);
            publicacion.setClub(club);
            club.getPublicaciones().add(publicacion);

            repositorioPublicacion.guardar(publicacion);
            repositorioClub.guardar(club);
        }
    }

    @Override
    public void eliminarPublicacion(Publicacion publicacion, Club club){
        if (publicacion != null && club != null) {
            club.getPublicaciones().remove(publicacion);
            repositorioClub.guardar(club);
            repositorioPublicacion.eliminar(publicacion);
        }
    }
}
