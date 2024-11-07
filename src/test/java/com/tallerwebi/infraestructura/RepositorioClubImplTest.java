
package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Puntuacion;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration (classes = {HibernateTestConfigRepositorio.class})
@Transactional
public class RepositorioClubImplTest {

    private RepositorioClubImpl repositorioClub;
    private RepositorioUsuarioImpl repositorioUsuario;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        this.repositorioClub = new RepositorioClubImpl(sessionFactory);
        this.repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

    @Rollback
    @Test
    public void dadoQueElClubExiste_searchClubDebeDevolverTrue() {
        Club club = new Club();
        club.setNombre("Club 1");
        this.repositorioClub.guardar(club);

        String hql = "from Club where nombre = :nombre";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Club 1");

        Club resultado = (Club) query.getSingleResult();
        assertThat(resultado.getNombre(), equalTo(club.getNombre()));
    }
    @Rollback
    @Test
    public void obtenerTodosLosClubs_deberiaDevolverListaDeClubs() {
        Club club1 = new Club();
        club1.setNombre("Club 1");
        this.repositorioClub.guardar(club1);

        Club club2 = new Club();
        club2.setNombre("Club 2");
        this.repositorioClub.guardar(club2);

        List<Club> clubs = this.repositorioClub.obtenerTodosLosClubs();
        int resultado = 2;
        assertThat( clubs.size(), equalTo(resultado));
    }
    @Rollback
    @Test
    public void devolverListaVaciaSiNoEncuentraCoincidencia (){
        Club club1 = new Club();
        club1.setNombre("Club De Ficcion");
        this.repositorioClub.guardar(club1);
        List<Club> clubs = this.repositorioClub.buscarClubPorNombre("Club De Misterio");
        assertThat(clubs, is(empty()));
    }
    @Rollback
    @Test
    public void obtenerPorNombreDelClub (){
        Club club1 = new Club();
        club1.setNombre("Club 1");
        this.repositorioClub.guardar(club1);
        Club club2 = new Club();
        club2.setNombre("Club 2");
        this.repositorioClub.guardar(club2);
        String hql = "from Club where nombre = :nombre";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Club 1");
        Club resultado = (Club) query.getSingleResult();
        assertThat(resultado.getNombre(), equalTo(club1.getNombre()));
    }


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

    @Test
    public void dadoElMetodoBuscarTodosLosClubsSiTengo2ClubsEnLaBDDMeDevuelveUnaListaDe2Posiciones(){
        Club club1 = new Club();
        Club club2 = new Club();

        repositorioClub.guardar(club1);
        repositorioClub.guardar(club2);

        List<Club> clubs = repositorioClub.obtenerTodosLosClubs();

        String hql = "FROM Club";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        List<Club> clubsEncontrados =  query.getResultList();

        assertThat(clubsEncontrados, equalTo(clubs));
    }

    @Test
    public void dadoElMetodoObtenerClubsConMasMiembrosSiTengo2ClubsLosOrdenaSegunSusMiembros(){
        Club club1 = new Club();
        Club club2 = new Club();
        club1.setCantidadMiembros(10);
        club2.setCantidadMiembros(20);

        repositorioClub.guardar(club1);
        repositorioClub.guardar(club2);

        List<Club> clubsObtenidos = repositorioClub.obtenerClubsConMasMiembros();

        assertThat(clubsObtenidos.get(0).getCantidadMiembros(), equalTo(20));
        assertThat(clubsObtenidos.get(1).getCantidadMiembros(), equalTo(10));
    }


    @Test
    public void dadoElMetodoObtenerClubsConMasMiembrosSiTengo6ClubsLosOrdenaSegunSusMiembrosConUnMaximoDe5Posiciones(){
        //Crea los clubs, setea sus miembros y los guarda en la bdd
        for (int i = 0; i < 10; i++) {
            Club club = new Club();
            club.setCantidadMiembros(i);
            repositorioClub.guardar(club);
        }

        List<Club> clubsObtenidos = repositorioClub.obtenerClubsConMasMiembros();

        assertThat(clubsObtenidos.size(), equalTo(5));
        assertThat(clubsObtenidos.get(0).getCantidadMiembros(), equalTo(9));
        assertThat(clubsObtenidos.get(1).getCantidadMiembros(), equalTo(8));
    }

    @Test
    public void dadoElMetodoObtenerClubsConMejorPuntajeSiTengo2ClubsLosOrdenaSegunSusPuntajes(){
        Club club1 = new Club();
        Club club2 = new Club();
        club1.setPuntuacionPromedio(2.0);
        club2.setPuntuacionPromedio(3.3);

        repositorioClub.guardar(club1);
        repositorioClub.guardar(club2);

        List<Club> clubsObtenidos = repositorioClub.obtenerClubsConMejorPuntuacion();

        assertThat(clubsObtenidos.get(0).getPuntuacionPromedio(), equalTo(3.3));
        assertThat(clubsObtenidos.get(1).getPuntuacionPromedio(), equalTo(2.0));
    }

    @Test
    public void dadoElMetodoObtenerClubsConMejorPuntuacionSiTengo6ClubsLosOrdenaSegunSusPuntajesConUnMaximoDe5Posiciones(){
        //Crea los clubs, setea sus miembros y los guarda en la bdd
        for (int i = 0; i < 10; i++) {
            Club club = new Club();
            club.setPuntuacionPromedio((double) i);
            repositorioClub.guardar(club);
        }

        List<Club> clubsObtenidos = repositorioClub.obtenerClubsConMejorPuntuacion();

        assertThat(clubsObtenidos.size(), equalTo(5));
        assertThat(clubsObtenidos.get(0).getPuntuacionPromedio(), equalTo(9.0));
        assertThat(clubsObtenidos.get(1).getPuntuacionPromedio(), equalTo(8.0));
    }

}

