package com.tallerwebi.dominio;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioLikeImpl implements ServicioLike{

    private RepositorioLike repositorioLike;
    private RepositorioComentario repositorioComentario;

    @Autowired
    public ServicioLikeImpl(RepositorioLike repositorioLike, RepositorioComentario repositorioComentario) {
        this.repositorioLike = repositorioLike;
        this.repositorioComentario = repositorioComentario;
    }

    @Override
    public Boolean agregarLike(Long comentarioId, Usuario autorDelLike) {
        Boolean dioLike = repositorioLike.validarSiUnUsuarioDioLikeAUnComentario(comentarioId,autorDelLike.getId());
        if (!dioLike){
            Like like = new Like();
            like.setAutorDelLike(autorDelLike);
            Comentario comentario = repositorioComentario.buscarComentarioPorId(comentarioId);
            like.setComentario(comentario);
            repositorioLike.agregarLike(like);
            return true;
        }
        return false;
    }

    @Override
    public Integer obtenerCantidadDeLikesDeUnComentario(Long comentarioId) {
        return repositorioLike.obtenerCantidadDeLikesDeUnComentario(comentarioId);
    }

    @Override
    public Boolean quitarLikeDeUnUsuario(Long comentarioId, Usuario usuario) {
        return repositorioLike.quitarLikeDeUnUsuario(comentarioId, usuario.getId());
    }
}
