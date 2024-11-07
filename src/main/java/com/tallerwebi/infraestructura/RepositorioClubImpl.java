package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Puntuacion;
import com.tallerwebi.dominio.RepositorioClub;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
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
    public List<Club> obtenerClubsConMejorPuntuacion() {
        String hql = "FROM Club c ORDER BY c.puntuacionPromedio DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql, Club.class);
        query.setMaxResults(5); //SOLO DEVUELVE 5 CLUBS
        return query.getResultList();
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
        sessionFactory.getCurrentSession().merge(puntuacionClub);
    }

    @Override
    public void eliminarPuntuacion(Puntuacion puntuacionClub) {
        sessionFactory.getCurrentSession().delete(puntuacionClub);
    }

    @Override
    public void actualizarPromedio(Long id, Double promedio) {
        String hql = "UPDATE Club c SET c.puntuacionPromedio = :puntuacionPromedio WHERE c.id = :clubId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("puntuacionPromedio", promedio);
        query.setParameter("clubId", id);
        query.executeUpdate();
    }
}
