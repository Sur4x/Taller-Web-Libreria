package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioComentarioImpl implements ServicioComentario{

    private RepositorioPublicacion repositorioPublicacion;
    private RepositorioComentario repositorioComentario;

    @Autowired
    public ServicioComentarioImpl(RepositorioPublicacion repositorioPublicacion, RepositorioComentario repositorioComentario) {
        this.repositorioPublicacion = repositorioPublicacion;
        this.repositorioComentario = repositorioComentario;
    }

    @Override
    public void guardarComentario(Comentario comentario, Publicacion publicacion) {

        if (publicacion != null && comentario != null){
            publicacion.getComentarios().add(comentario);
            repositorioComentario.guardar(comentario);
            repositorioPublicacion.guardar(publicacion);
        }
    }
}
