package com.tallerwebi.dominio;

import com.tallerwebi.dominio.Logro;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RepositorioLogro {

    @Autowired
    private SessionFactory sessionFactory;

    public void guardar(Logro logro) {
        sessionFactory.getCurrentSession().saveOrUpdate(logro);
    }

    public Logro buscarLogroActivo(Long usuarioId) {
        return (Logro) sessionFactory.getCurrentSession()
                .createQuery("FROM Logro WHERE usuario.id = :usuarioId AND cumplido = false", Logro.class)
                .setParameter("usuarioId", usuarioId)
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<Logro> contarLogrosCumplidosPorTipo(Long usuarioId, String tipo) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Logro WHERE usuario.id = :usuarioId AND cumplido = true AND tipo = :tipo", Logro.class)
                .setParameter("usuarioId", usuarioId)
                .setParameter("tipo", tipo)
                .list();
    }
}
