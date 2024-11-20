package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository("repositorioPublicacion")
public class RepositorioPublicacionImpl implements RepositorioPublicacion {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPublicacionImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public Publicacion buscarPublicacionPorId(Long idPublicacion) {

        final Session session = sessionFactory.getCurrentSession();
        return (Publicacion) session.createCriteria(Publicacion.class)
                .add(Restrictions.eq("id", idPublicacion))
                .uniqueResult();
    }

    @Override
    public void guardar(Publicacion publicacion){
        sessionFactory.getCurrentSession().saveOrUpdate(publicacion);
    }

    @Override
    public void eliminar(Publicacion publicacion){
        sessionFactory.getCurrentSession().delete(publicacion);
    }

    @Override
    public Publicacion buscarPublicacionEnUnClub(Long publicacionId, Club club) {
        Long clubId = club.getId();
        String hql = "FROM Publicacion p WHERE p.id = :publicacionId AND p.club.id = :clubId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("publicacionId",publicacionId);
        query.setParameter("clubId",clubId);
        List<Publicacion> resultList = query.getResultList(); //si no encuentra una publicacion no tire excepcion
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    @Override
    public List<Publicacion> buscarPublicacionesMasRecientesDeUsuariosSeguidos(Set<Usuario> usuariosSeguidos){
        List<Long> usuariosSeguidosIds = new ArrayList<>();

        for(Usuario usuario : usuariosSeguidos){
            usuariosSeguidosIds.add(usuario.getId());
        }

        String hql = "FROM Publicacion p WHERE p.usuario.id IN :usuariosSeguidos ORDER BY p.id DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuariosSeguidos", usuariosSeguidosIds);
        query.setMaxResults(5); //DEVUELVE LAS 5 PUBLICACIONES MÁS RECIENTES DE SUS SEGUIDORES
        List <Publicacion> publicacionesUsuariosSeguidos = query.getResultList();
        return publicacionesUsuariosSeguidos.isEmpty() ? Collections.emptyList() : publicacionesUsuariosSeguidos;
    }

}
