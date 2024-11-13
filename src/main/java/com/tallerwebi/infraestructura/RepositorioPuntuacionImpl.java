package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class RepositorioPuntuacionImpl implements RepositorioPuntuacion {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPuntuacionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Puntuacion buscarPuntuacion(Club club, Usuario usuario) {
        try{
            String hql = "FROM Puntuacion p WHERE p.club = :club AND p.usuario = :usuario";
            Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("club", club);
            query.setParameter("usuario", usuario);
            Puntuacion puntuacion = (Puntuacion) query.getSingleResult();
            return puntuacion;
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void guardarPuntuacion(Puntuacion puntuacionClub) {
        sessionFactory.getCurrentSession().merge(puntuacionClub);
    }

    @Override
    public void eliminarPuntuacion(Puntuacion puntuacionClub) {
        sessionFactory.getCurrentSession().delete(puntuacionClub);
    }

    /*
    @Override
    @Transactional
    public void actualizarPromedio(Long id, Double promedio) {
        String hql = "UPDATE Club c SET c.puntuacionPromedio = :puntuacionPromedio WHERE c.id = :clubId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("puntuacionPromedio", promedio);
        query.setParameter("clubId", id);
        query.executeUpdate();

    }

     */
}
