package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.RepositorioClub;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
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
    public List<Club> obtenerTodosLosClubs() {
        String hql = "FROM Club";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);

        return query.getResultList();
    }

    @Override
    @Transactional
    public Club buscarClubPor(Long id) {
        String hql = "FROM Club WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);

        return (Club) query.getSingleResult();
    }



    @Override
    public void guardar(Club club) {
        sessionFactory.getCurrentSession().save(club);
    }

}

