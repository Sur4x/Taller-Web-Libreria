package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comentario;
import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.RepositorioComentario;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioComentarioImpl implements RepositorioComentario {


    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioComentarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Comentario comentario){
        sessionFactory.getCurrentSession().saveOrUpdate(comentario);
    }

    @Override
    public Comentario buscarComentarioEnUnaPublicacion(Long comentarioId, Publicacion publicacion) {
        Long publicacionId = publicacion.getId();
        String hql = "FROM Comentario c WHERE c.id = :comentarioId AND c.publicacion.id = :publicacionId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("comentarioId", comentarioId);
        query.setParameter("publicacionId", publicacionId);
        List<Comentario> resultList = query.getResultList(); // Si no encuentra un comentario, no tira excepción
        return resultList.isEmpty() ? null : resultList.get(0);
    }


    @Override
    public void eliminarComentario(Comentario comentario) {
        Long comentarioId = comentario.getId();
        String hql = "DELETE FROM Comentario c WHERE c.id = :comentarioId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("comentarioId", comentarioId);
        query.executeUpdate();
    }

    @Override
    public Comentario buscarComentarioPorId(Long comentarioId) {
        String hql = "FROM Comentario c WHERE c.id = :comentarioId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("comentarioId", comentarioId);

        return (Comentario) query.getSingleResult();
    }

}
