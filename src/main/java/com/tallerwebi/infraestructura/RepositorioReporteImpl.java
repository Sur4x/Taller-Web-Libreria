package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.Reporte;
import com.tallerwebi.dominio.RepositorioReporte;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("repositorioReporte")
public class RepositorioReporteImpl implements RepositorioReporte {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioReporteImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public Reporte buscarReportePorId(Long idReporte) {
        final Session session = sessionFactory.getCurrentSession();
        return (Reporte) session.createCriteria(Reporte.class)
                .add(Restrictions.eq("id", idReporte))
                .uniqueResult();
    }

    @Override
    public void guardar(Reporte reporte) {
        sessionFactory.getCurrentSession().saveOrUpdate(reporte);
    }

    @Override
    public void eliminar(Reporte reporte) {
        sessionFactory.getCurrentSession().delete(reporte);
    }

    @Override
    public List<Reporte> obtenerTodosLosReportesDeUnClub(Club idClub) {
        final Session session = sessionFactory.getCurrentSession();
        return (List<Reporte>) session.createCriteria(Reporte.class)
                .add(Restrictions.eq("club", idClub))
                .list();
    }
}
