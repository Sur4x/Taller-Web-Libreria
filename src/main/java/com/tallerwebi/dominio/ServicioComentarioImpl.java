package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioComentarioImpl implements ServicioComentario{

    @Autowired
    private RepositorioPublicacion repositorioPublicacion;

    @Autowired
    private RepositorioComentario repositorioComentario;

    @Override
    public void guardarComentario(Comentario comentario, Publicacion publicacion) {
       publicacion.getComentarios().add(comentario);
       repositorioComentario.guardar(comentario);
       repositorioPublicacion.guardar(publicacion);
    }
}
