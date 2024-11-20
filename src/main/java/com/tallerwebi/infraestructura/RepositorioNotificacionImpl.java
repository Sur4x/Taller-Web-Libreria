package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.dominio.Reporte;
import com.tallerwebi.dominio.RepositorioNotificacion;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioNotificacionImpl implements RepositorioNotificacion {

    private SessionFactory sessionFactory;
    @Autowired
    public RepositorioNotificacionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void crearNotificacion(Notificacion notificacion) {
        sessionFactory.getCurrentSession().saveOrUpdate(notificacion);
    }

    @Override
    public Notificacion buscarNotificacionPor(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return (Notificacion) session.createCriteria(Notificacion.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public List<Notificacion> listarTodasLasNotificaciones() {
        final Session session = sessionFactory.getCurrentSession();
        return (List<Notificacion>) session.createCriteria(Notificacion.class)
                .list();
    }
}
