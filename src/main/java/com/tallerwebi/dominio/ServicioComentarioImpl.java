package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioComentarioImpl implements ServicioComentario{

    private RepositorioPublicacion repositorioPublicacion;
    private RepositorioComentario repositorioComentario;
    private RepositorioLike repositorioLike;

    @Autowired
    public ServicioComentarioImpl(RepositorioPublicacion repositorioPublicacion, RepositorioComentario repositorioComentario, RepositorioLike repositorioLike) {
        this.repositorioPublicacion = repositorioPublicacion;
        this.repositorioComentario = repositorioComentario;
        this.repositorioLike = repositorioLike;
    }

    @Override
    public void guardarComentario(Comentario comentario, Publicacion publicacion) {
        if (publicacion != null && comentario != null){
            publicacion.getComentarios().add(comentario);
            repositorioComentario.guardar(comentario);
            repositorioPublicacion.guardar(publicacion);
        }
    }

    @Override
    public void setearAutorYPublicacionEnUnComentario(Comentario comentario, Usuario autor, Publicacion publicacion) {
        comentario.setAutor(autor);
        comentario.setPublicacion(publicacion);
    }

    @Override
    public Comentario buscarComentarioEnUnaPublicacion(Long comentarioId, Publicacion publicacion) {
        return repositorioComentario.buscarComentarioEnUnaPublicacion(comentarioId,publicacion);
    }

    @Override
    public void eliminarComentario(Comentario comentario) {
        repositorioLike.eliminarLikesDeUnComentario(comentario.getId());
        repositorioComentario.eliminarComentario(comentario);
    }

}
