package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comentario;
import com.tallerwebi.dominio.Like;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestConfigRepositorio;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Query;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfigRepositorio.class})
@Transactional
public class RepositorioLikeTest {

    private RepositorioLikeImpl repositorioLike;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        this.repositorioLike = new RepositorioLikeImpl(sessionFactory);
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnRepositorioLikeCuandoGuardoUnLikeLoPuedoObtenerDelaBDD() {
        // Creo el usuario y el comentario y los guardo en la BDD. despues recien ahi agrego el like a la BDD
        Usuario usuario = new Usuario();
        usuario.setEmail("usuarioEjemplo");
        sessionFactory.getCurrentSession().save(usuario);

        Comentario comentario = new Comentario();
        comentario.setTexto("Comentario de prueba");
        sessionFactory.getCurrentSession().save(comentario);

        Like like = new Like();
        like.setAutorDelLike(usuario);
        like.setComentario(comentario);

        this.repositorioLike.agregarLike(like);

        String hql = "FROM Like l WHERE l.autorDelLike.id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", usuario.getId());
        Like likeObtenido = (Like) query.getSingleResult();

        assertThat(likeObtenido.getAutorDelLike().getId(), equalTo(usuario.getId()));
        assertThat(likeObtenido.getComentario().getId(), equalTo(comentario.getId()));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnRepositorioLikeCuandoEliminoUnLikeExistenteLoEliminaDeLaBDDCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuarioEjemplo");
        sessionFactory.getCurrentSession().save(usuario);

        Comentario comentario = new Comentario();
        comentario.setTexto("Comentario de prueba");
        sessionFactory.getCurrentSession().save(comentario);

        Like like = new Like();
        like.setAutorDelLike(usuario);
        like.setComentario(comentario);
        this.repositorioLike.agregarLike(like);

        String hql = "DELETE FROM Like l WHERE l.autorDelLike.id = :usuarioId and comentario.id = : comentarioId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuario.getId());
        query.setParameter("comentarioId", comentario.getId());
        int filasAfectadas = query.executeUpdate();

        assertThat(filasAfectadas, equalTo(1));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnRepositorioLikeCuandoEliminoUnLikeQueNoExisteNoEsPosibleEliminarlo() {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuarioEjemplo");
        sessionFactory.getCurrentSession().save(usuario);

        Comentario comentario = new Comentario();
        comentario.setTexto("Comentario de prueba");
        sessionFactory.getCurrentSession().save(comentario);
        //Nunca agrego el like a la base de datos

        String hql = "DELETE FROM Like l WHERE l.autorDelLike.id = :usuarioId and comentario.id = : comentarioId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuario.getId());
        query.setParameter("comentarioId", comentario.getId());
        int filasAfectadas = query.executeUpdate();

        assertThat(filasAfectadas, equalTo(0));
    }
}
