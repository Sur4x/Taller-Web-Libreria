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

//    @Test
//    @Rollback
//    @Transactional
//    public void dadoUnaPuntuacionGuardadaCuandoSeEliminaEntoncesNoSeEncuentraEnLaBaseDeDatos() {
//        Club club = new Club();
//        Usuario usuario = new Usuario();
//        Integer valorPuntuacion = 5;
//
//        Puntuacion puntuacion = new Puntuacion(club, usuario, valorPuntuacion);
//        repositorioClub.guardar(club);
//        repositorioUsuario.guardar(usuario);
//        repositorioPuntuacion.guardarPuntuacion(puntuacion);
//
//        repositorioPuntuacion.eliminarPuntuacion(puntuacion);
//
//        String hql = "FROM Puntuacion p WHERE p.club = :club AND p.usuario = :usuario";
//        Query query = sessionFactory.getCurrentSession().createQuery(hql);
//        query.setParameter("club", club);
//        query.setParameter("usuario", usuario);
//
//        List<Puntuacion> resultados = query.getResultList();
//
//
//        assertThat(resultados, empty());
//    }








    /* ESTOS TEST LOS HICE PARA EL ANTERIOR METODO repositorioClub.actualizarPromedio(club.getId(), promedio);
    @Rollback
    @Test
    public void dadoElMetodoGuardarPuntuacionCuandoGuardaUnaPuedoObtenerlaDeLaBDD(){
        Usuario usuario = new Usuario();
        this.repositorioUsuario.guardar(usuario);

        Club club = new Club();
        this.repositorioClub.guardar(club);

        Puntuacion puntuacion = new Puntuacion();
        puntuacion.setClub(club);
        puntuacion.setUsuario(usuario);
        puntuacion.setPuntuacion(4);

        this.repositorioClub.guardarPuntuacion(puntuacion);

        String hql = "from Puntuacion where club.id = :clubId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("clubId", club.getId());

        Puntuacion puntuacionObtenida = (Puntuacion) query.getSingleResult();

        assertThat(puntuacionObtenida.getPuntuacion(), equalTo(puntuacion.getPuntuacion()));
        assertThat(puntuacionObtenida.getClub().getId(), equalTo(club.getId()));
        assertThat(puntuacionObtenida.getUsuario().getId(), equalTo(usuario.getId()));
    }

    @Rollback
    @Test
    public void dadoElMetodoActualizarPromedioCuandoTengoUnClubEnLaBDDConUnPromedioPuedoActualizarloPorOtro() {
        Club club = new Club();
        club.setPuntuacionPromedio(1.0);
        this.repositorioClub.guardar(club);

        this.repositorioClub.actualizarPromedio(club.getId(), 3.5);

        String hql = "from Club where id = :clubId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("clubId", club.getId());
        Club clubObtenido = (Club) query.getSingleResult();

        assertThat(clubObtenido, equalTo(club));
        assertThat(clubObtenido.getPuntuacionPromedio(), equalTo(3.5));
    }


 */
}
