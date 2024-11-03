package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Like;
import com.tallerwebi.dominio.RepositorioLike;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
@Repository
public class RepositorioLikeImpl implements RepositorioLike {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioLikeImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Boolean validarSiUnUsuarioDioLikeAUnComentario(Long comentarioId, Long idAutorDelLike) {
        String hql = "SELECT COUNT(l.id) FROM Like l WHERE l.comentario.id = :comentarioId AND l.autorDelLike.id = :idAutorDelLike";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("comentarioId", comentarioId);
        query.setParameter("idAutorDelLike", idAutorDelLike);

        Long count = (Long) query.getSingleResult();

        return count != null && count > 0;
    }

    @Override
    public void agregarLike(Like like) {
        sessionFactory.getCurrentSession().saveOrUpdate(like);
    }

    @Override
    public Integer obtenerCantidadDeLikesDeUnComentario(Long comentarioId) {
        String hql = "SELECT COUNT(l) FROM Like l WHERE l.comentario.id = :comentarioId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("comentarioId", comentarioId);
        Long contador = (Long) query.getSingleResult();
        return contador.intValue();
    }

    @Override
    public void eliminarLikesDeUnComentario(Long comentarioId) {
        String hql = "DELETE FROM Like l WHERE l.comentario.id = :comentarioId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("comentarioId", comentarioId);
        query.executeUpdate();
    }
}
