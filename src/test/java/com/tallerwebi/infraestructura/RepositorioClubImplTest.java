
package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.Club;
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

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        this.repositorioClub = new RepositorioClubImpl(sessionFactory);
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

/*
    @Rollback
    @Test
    public void dadoElMetodoIncrementarCantidadDeReportesEnUnClubSiReportoUnClub2VecesSuCantidadDeReportesEs2() {
        Club club = new Club();
        club.setId(1L);
        club.setCantidadDeReportes(0);
        this.repositorioClub.guardar(club);

        this.repositorioClub.incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(club.getId());
        Integer reportesTotales = this.repositorioClub.incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(club.getId());


        assertThat(reportesTotales, is(2));
    }
 */
}

