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
import java.util.ArrayList;
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
    public Club buscarClubPor(Long id) {
        String hql = "FROM Club c WHERE c.id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        List<Club> clubs = query.getResultList();
        return clubs.isEmpty() ? null : clubs.get(0);
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
    public void eliminar(Club club) {
        sessionFactory.getCurrentSession().delete(club);
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

    @Override
    public List<Club> obtenerClubsConMasMiembros() {
        String hql = "FROM Club c ORDER BY c.cantidadMiembros DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql, Club.class);
        query.setMaxResults(5); //SOLO DEVUELVE 5 CLUBS
        return query.getResultList();
    }

    @Override
    public List<Club> obtenerClubsConMejorCalificacion() {
        String hql = "FROM Club c ORDER BY c.calificacion DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql, Club.class);
        query.setMaxResults(5); //SOLO DEVUELVE 5 CLUBS
        return query.getResultList();
    }
}
