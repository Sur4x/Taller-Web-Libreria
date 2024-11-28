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
    public void refrescarClub(Club club) {
        sessionFactory.getCurrentSession().refresh(club);
    }

    @Override
    public Double obtenerPromedioDeUnClub(Long id) {
        String hql = "SELECT AVG(p.puntuacion) FROM Puntuacion p WHERE p.club.id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        Double promedio = (Double) query.getSingleResult();
        return promedio != null ? promedio : 0.0;
    }

    @Override
    public boolean existeUnClubConEsteNombre(String nombre) {
        String hql = "SELECT COUNT(c) FROM Club c WHERE c.nombre = :nombre";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", nombre);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    @Override
    public void actualizarPromedioDeUnClub(Long idClub, Double nuevoPromedio) {
        String hql = "UPDATE Club c SET c.puntuacionPromedio = :nuevoPromedio WHERE c.id = :idClub";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nuevoPromedio", nuevoPromedio);
        query.setParameter("idClub", idClub);
        query.executeUpdate();
    }
}
