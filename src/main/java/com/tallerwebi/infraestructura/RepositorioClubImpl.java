package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.RepositorioClub;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioClubImpl implements RepositorioClub {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioClubImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
        return (Club) sessionFactory.getCurrentSession().createCriteria(Club.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public void guardar(Club club) {
        sessionFactory.getCurrentSession().saveOrUpdate(club);
    }

    @Override
    public List<Club> buscarClubPorNombre(String nombre) {
        String hql = "FROM Club c WHERE LOWER(c.nombre) LIKE LOWER(:nombre)";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "%" + nombre + "%"); // Coincidencia parcial

        return query.getResultList();
    }

    @Override
    public void eliminar(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        Club club = session.get(Club.class, id);
        if (club != null) {
            session.delete(club);
        }
    }

}
