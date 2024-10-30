package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Comentario;
import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.infraestructura.config.HibernateTestConfigRepositorio;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.transaction.Transactional;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfigRepositorio.class})
@Transactional
public class RepositorioComentarioImplTest {

    private RepositorioComentarioImpl repositorioComentario;
    private RepositorioPublicacionImpl repositorioPublicacion;
    private RepositorioClubImpl repositorioClub;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        repositorioComentario = new RepositorioComentarioImpl(sessionFactory);
        repositorioPublicacion = new RepositorioPublicacionImpl(sessionFactory);
        repositorioClub = new RepositorioClubImpl(sessionFactory);
    }

    @Rollback
    @Test
    public void dadoQueElComentarioSeGuarda_guardarDebePersistirComentario(){
        Comentario comentario = new Comentario();
        comentario.setTexto("Este es un comentario de prueba");
        repositorioComentario.guardar(comentario);
        String hql = "from Comentario where texto = :texto";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("texto", "Este es un comentario de prueba");
        Comentario comentarioGuardado = (Comentario) query.getSingleResult();
        assertThat(comentarioGuardado.getTexto(), equalTo(comentario.getTexto()));

    }
    @Rollback
    @Test
    public void dadoElMetodoBuscarComentarioEnUnaPublicacionSiLoEncuentraRetornaElComentario() {
        Club club = new Club();
        repositorioClub.guardar(club);
        Publicacion publicacion1 = new Publicacion();
        publicacion1.setClub(club);
        repositorioPublicacion.guardar(publicacion1);

        Comentario comentario = new Comentario();
        comentario.setPublicacion(publicacion1);
        repositorioComentario.guardar(comentario);

        Comentario comentarioObtenido = repositorioComentario.buscarComentarioEnUnaPublicacion(comentario.getId(), publicacion1);
        assertThat(comentarioObtenido.getId(), equalTo(1L));
    }


    @Rollback
    @Test
    public void dadoElMetodoBuscarComentarioEnUnaPublicacionSiNoLoEncuentraRetornaNull(){
        Publicacion publicacion = new Publicacion();
        repositorioPublicacion.guardar(publicacion);

        Comentario comentarioObtenido = repositorioComentario.buscarComentarioEnUnaPublicacion(1L, publicacion);
        assertThat(comentarioObtenido, equalTo(null));
    }

    @Rollback
    @Test
    public void dadoElMetodoEliminarComentarioSiTengoUnComentarioEnElSistemaLoBorra(){
        Comentario comentario = new Comentario();
        //comentario.setId(1L);
        Publicacion publicacion = new Publicacion();
        publicacion.setComentarios(new ArrayList<>());
        publicacion.getComentarios().add(comentario);
        repositorioComentario.guardar(comentario);

        repositorioComentario.eliminarComentario(comentario);
        Comentario comentarioObtenido = repositorioComentario.buscarComentarioEnUnaPublicacion(1L,publicacion);
        assertThat(comentarioObtenido, equalTo(null));
    }

}
