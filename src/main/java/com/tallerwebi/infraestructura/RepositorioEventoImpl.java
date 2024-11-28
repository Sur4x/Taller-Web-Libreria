package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RepositorioEventoImpl implements RepositorioEvento {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioEventoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Evento evento) {
        {
            sessionFactory.getCurrentSession().saveOrUpdate(evento);
        }
    }

    @Override
    public Evento buscarEventoValido(Club club) {
        Long clubId = club.getId();
        LocalDateTime ahora = LocalDateTime.now();

        // HQL: Filtrar por club, estado "Activo", o eventos cuya fecha sea futura
        String hql = "FROM Evento e " +
                "WHERE e.club.id = :clubId " +
                "AND (e.estado = 'activo' OR e.fechaYHora > :ahora) " +
                "ORDER BY e.fechaYHora ASC";

        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("clubId", clubId);
        query.setParameter("ahora", ahora);

        List<Evento> eventos = query.getResultList();

        if (eventos.isEmpty()) {
            return null; // No se encontró ningún evento válido
        } else {
            return eventos.get(0); // Retorna el evento más próximo o activo
        }
    }


    @Override
    public List<Evento> buscarEventoPorEstado(String estado) {
        String hql = "FROM Evento e WHERE e.estado = :estado";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("estado", estado);
        return query.getResultList();
    }

    @Override
    public Evento buscarEventoPorId(Long id) {
        String hql = "FROM Evento e WHERE e.id = :id";  // :id es el parámetro
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        return (Evento) query.getSingleResult();
    }

    @Override
    public List<Mensaje> buscarComentariosEnUnEvento(Evento evento) {
        String hql = "SELECT mensajes FROM Evento e WHERE e.id = :id";  // :id es el parámetro
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", evento.getId());
        return query.getResultList();
    }

    @Override
    public void finalizar(Evento evento) {
        String hql = "UPDATE Evento e SET e.estado = :estado WHERE e.id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("estado", "finalizado");
        query.setParameter("id", evento.getId());
        query.executeUpdate();
    }

}
