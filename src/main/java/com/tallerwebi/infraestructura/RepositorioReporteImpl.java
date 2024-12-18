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

import javax.persistence.Query;
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
    public List<Reporte> obtenerTodosLosReportesDeUnClub(Long idClub) {
        String hql = "FROM Reporte r WHERE r.club.id = :idClub AND r.estado = 'pendiente'";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idClub", idClub);
        List<Reporte> reportes = query.getResultList();
        return reportes;
    }

    @Override
    public List<Reporte> obtenerTodosLosReportesAprobadosDeUnClub(Long idClub) {
        String hql = "FROM Reporte r WHERE r.club.id = :idClub AND r.estado = 'aprobado'";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idClub", idClub);
        List<Reporte> reportes = query.getResultList();
        return reportes;
    }

    @Override
    public void aprobarReporte(Long idReporte) {
        String hql = "UPDATE Reporte r SET r.estado = 'aprobado' WHERE r.id = :idReporte";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idReporte", idReporte);
        query.executeUpdate();
    }

    @Override
    public Integer obtenerCantidadDeReportesDeUnClub(Long idClub) {
        String hql = "SELECT COUNT(r) FROM Reporte r WHERE r.club.id = :idClub AND r.estado = 'aprobado'";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idClub", idClub);
        Long result = (Long) query.getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    @Override
    @Transactional
    public Integer incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(Long idClub) {
        String hql = "UPDATE Club SET cantidadDeReportes = cantidadDeReportes + 1 WHERE id = :idClub";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idClub", idClub);
        query.executeUpdate();

        // Ahora obtiene la cantidad actual de reportes
        String hqlSelect = "SELECT c.cantidadDeReportes FROM Club c WHERE c.id = :idClub";
        Query selectQuery = this.sessionFactory.getCurrentSession().createQuery(hqlSelect);
        selectQuery.setParameter("idClub", idClub);
        return (Integer) selectQuery.getSingleResult();
    }

    public Boolean comprobarSiElUsuarioReportoPreviamente(Long idUsuario, Long idClub){
        String hql = "SELECT COUNT(r) FROM Reporte r WHERE r.usuario.id = :idUsuario AND r.club.id = :idClub";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idClub", idClub);
        Long count = (Long) query.getSingleResult(); // Obtiene el resultado único (el conteo)
        return count != null && count > 0;
    }
}
