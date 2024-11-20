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
        sessionFactory.getCurrentSession().saveOrUpdate(puntuacionClub);
    }

    @Override
    public void eliminarPuntuacion(Long idClub, Long idUsuario) {
        String hql = "DELETE FROM Puntuacion p WHERE p.club.id = :idClub AND p.usuario.id = :idUsuario";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idClub", idClub);
        query.setParameter("idUsuario", idUsuario);
        query.executeUpdate();
    }
}
