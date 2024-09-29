package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.RepositorioClub;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioClubImpl implements RepositorioClub {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioClubImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Boolean searchClub(Club club) {
        final Session session = sessionFactory.getCurrentSession();
        Club resultado =  (Club) session.createCriteria(Club.class)
                .add(Restrictions.eq("nombre", club.getNombre()))
                .add(Restrictions.eq("descripcion", club.getDescripcion()))
                .uniqueResult();

        if (resultado != null) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Boolean addClub(Club club) {
        Session session = sessionFactory.getCurrentSession();
        try{
            session.save(club);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Club> obtenerTodosLosClubs() {
        String hql = "FROM Club";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);

        return query.getResultList();

    }

    @Override
    public Club buscarClubPor(Long id) {
        String hql = "FROM Club WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);

        // Si esperas un único resultado
        return (Club) query.getSingleResult();
    }

}

