package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comentario;
import com.tallerwebi.dominio.RepositorioComentario;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioComentarioImpl implements RepositorioComentario {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void guardar(Comentario comentario){
        sessionFactory.getCurrentSession().saveOrUpdate(comentario);
    }
}
