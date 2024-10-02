package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.RepositorioClub;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioClubImpl implements RepositorioClub {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioClubImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Boolean searchClub(Club club) {
        final Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Club.class);
        criteria.add(Restrictions.eq("nombre", club.getNombre()));
        return criteria.uniqueResult() != null;
    }

    @Override
    public List<Club> obtenerTodosLosClubs() {
        final Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Club.class);
        return criteria.list();
    }

    @Override
    public Club buscarClubPor(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return session.get(Club.class, id);
    }

    @Override
    public void guardar(Club club) {
        sessionFactory.getCurrentSession().saveOrUpdate(club);
    }

    @Override
    public List<Club> buscarClubPorNombre(String nombre) {
        final Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Club.class);
        criteria.add(Restrictions.ilike("nombre", "%" + nombre + "%")); // Coincidencia parcial
        return criteria.list();
    }
}
