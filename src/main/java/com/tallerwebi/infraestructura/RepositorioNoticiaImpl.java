package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Noticia;
import com.tallerwebi.dominio.RepositorioNoticia;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;

import java.util.List;

@Repository
public class RepositorioNoticiaImpl implements RepositorioNoticia {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioNoticiaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarNoticia(Noticia noticia){
        sessionFactory.getCurrentSession().saveOrUpdate(noticia);
    }

    @Override
    public List<Noticia> obtenerNoticiasRandom(Integer cantidad){
        String hql = "FROM Noticia ORDER BY function('RAND')";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setMaxResults(cantidad);
        return query.getResultList();
    }
}
