package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.config.HibernateTestConfigRepositorio;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import javax.persistence.Query;
import javax.transaction.Transactional;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfigRepositorio.class})
@Transactional
public class RepositorioPuntuacionTest {

    private RepositorioPuntuacion repositorioPuntuacion;
    private RepositorioClub repositorioClub;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        this.repositorioPuntuacion = new RepositorioPuntuacionImpl(sessionFactory);
        this.repositorioClub = new RepositorioClubImpl(sessionFactory);
        this.repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

    @Test
    @Rollback
    @Transactional
    public void dadoUnaPuntuacionGuardadaEnLaBaseDeDatosLaPuedoEncontrarConElMetodoBuscarPuntuacion(){
        Club club = new Club();
        Usuario usuario = new Usuario();
        Integer valorPuntuacion = 5;

        Puntuacion puntuacion = new Puntuacion(club, usuario, valorPuntuacion);

        repositorioClub.guardar(club);
        repositorioUsuario.guardar(usuario);

        repositorioPuntuacion.guardarPuntuacion(puntuacion);

        Puntuacion obtenida = repositorioPuntuacion.buscarPuntuacion(club, usuario);

        assertThat(puntuacion.getUsuario(), equalTo(obtenida.getUsuario()));
        assertThat(puntuacion.getClub(), equalTo(obtenida.getClub()));
        assertThat(puntuacion.getPuntuacion(), equalTo(obtenida.getPuntuacion()));
    }

    @Test
    @Rollback
    public void dadoUnaPuntuacionCuandoSeGuardaYLuegoSeBuscaEntoncesRetornaLaMismaPuntuacion() {
        Club club = new Club();
        Usuario usuario = new Usuario();
        Integer valorPuntuacion = 5;

        Puntuacion puntuacion = new Puntuacion(club, usuario, valorPuntuacion);

        repositorioClub.guardar(club);
        repositorioUsuario.guardar(usuario);

        repositorioPuntuacion.guardarPuntuacion(puntuacion);

        String hql = "FROM Puntuacion p WHERE p.club = :club AND p.usuario = :usuario";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("club", club);
        query.setParameter("usuario", usuario);

        Puntuacion resultado = (Puntuacion) query.getSingleResult();

        assertThat(resultado.getClub(), equalTo(puntuacion.getClub()));
        assertThat(resultado.getUsuario(), equalTo(puntuacion.getUsuario()));
        assertThat(resultado.getPuntuacion(), equalTo(puntuacion.getPuntuacion()));
    }

    @Test
    @Rollback
    public void dadoUnaPuntuacionNoGuardadaEnLaBaseDeDatosNoLaPuedoEncontrarConElMetodoBuscarPuntuacion(){
        Club club = new Club();
        Usuario usuario = new Usuario();
        Integer valorPuntuacion = 5;

        Puntuacion puntuacion = new Puntuacion(club, usuario, valorPuntuacion);
        puntuacion.setIdPuntuacion(1L);

        repositorioClub.guardar(club);
        repositorioUsuario.guardar(usuario);

        Puntuacion obtenida = repositorioPuntuacion.buscarPuntuacion(club,usuario);

        assertThat(obtenida, equalTo(null));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoElMetodoEliminarPuntuacionCuandoExisteUnaPuntuacionEnLaBaseDeDatosLaElimina(){
        Club club = new Club();
        Usuario usuario = new Usuario();
        Integer valorPuntuacion = 5;

        Puntuacion puntuacion = new Puntuacion(club, usuario, valorPuntuacion);

        repositorioClub.guardar(club);
        repositorioUsuario.guardar(usuario);

        repositorioPuntuacion.guardarPuntuacion(puntuacion);

        String hql = "DELETE FROM Puntuacion p WHERE p.club.id = :idClub AND p.usuario.id = :idUsuario";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idClub", club.getId());
        query.setParameter("idUsuario", usuario.getId());
        query.executeUpdate();

        Puntuacion puntuacionEncontrada = repositorioPuntuacion.buscarPuntuacion(club,usuario);
        assertThat(puntuacionEncontrada, equalTo(null));

    }

}
