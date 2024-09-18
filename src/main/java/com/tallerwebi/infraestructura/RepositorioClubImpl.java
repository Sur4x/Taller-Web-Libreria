package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.RepositorioClub;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

@Repository("repositorioClub")
public class RepositorioClubImpl implements RepositorioClub {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioClubImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Club buscarClub(Long id, String nombre) {
        final Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Club.class);

        criteria.add(Restrictions.eq("id", id));
        criteria.add(Restrictions.eq("nombre", nombre));

        return (Club) criteria.uniqueResult();
    }

    @Override
    public void guardar(Club club) {
        sessionFactory.getCurrentSession().save(club);
    }

    @Override
    public Usuario buscarUsuarioEnClub(String nombreClub, String emailUsuario) {
        final Session session = sessionFactory.getCurrentSession();

        // Buscar el club por nombre
        Criteria clubCriteria = session.createCriteria(Club.class);
        clubCriteria.add(Restrictions.eq("nombre", nombreClub));
        Club club = (Club) clubCriteria.uniqueResult();

        if (club == null) {
            // Club no encontrado
            return null;
        }

        // Buscar el usuario en el club
        Criteria userCriteria = session.createCriteria(Usuario.class);
        userCriteria.createAlias("clubs", "club");
        userCriteria.add(Restrictions.eq("club.id", club.getId()));
        userCriteria.add(Restrictions.eq("email", emailUsuario));

        return (Usuario) userCriteria.uniqueResult();

    }

    @Override
    public void modificar(Club club) {
        sessionFactory.getCurrentSession().update(club);
    }
}
