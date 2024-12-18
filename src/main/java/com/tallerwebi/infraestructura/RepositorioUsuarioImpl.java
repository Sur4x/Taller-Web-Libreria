package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.RepositorioUsuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) {

        final Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))

                .uniqueResult();
    }

    @Override
    public void guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().saveOrUpdate(usuario);
    }

    @Override
    public Usuario buscar(String email) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public List<Usuario> buscarTodosLosUsuarios() {
        String hql = "FROM Usuario";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);

        return query.getResultList();
    }

    @Override
    public Usuario buscarUsuarioPor(Long id) {
        String hql = "FROM Usuario WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);

        // Si esperas un único resultado
        return (Usuario) query.getSingleResult();
    }

    @Override
    public List<Usuario> buscarUsuariosSeguidosPorUsuario(Usuario usuario){
        Long usuarioId = usuario.getId();

        String hql = "SELECT u FROM usuario u WHERE u.id IN (SELECT us.id.seguido FROM usuario.seguimiento us WHERE us.id.seguidor = : usuarioId)";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuario.getId());
        return query.getResultList();
    }


}
