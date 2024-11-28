package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Reporte;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
@Transactional
public class RepositorioReporteImplTest {

    private RepositorioReporteImpl repositorioReporte;
    private RepositorioClubImpl repositorioClub;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        repositorioReporte = new RepositorioReporteImpl(sessionFactory);
        repositorioClub = new RepositorioClubImpl(sessionFactory);
    }

   @Test
   public void dadoElMetodobuscarReportePorIdSiEncuentraElReporteMeLoDevuelve() {

       Reporte reporte = new Reporte();
       reporte.setDescripcion("Descripcion de prueba");

       repositorioReporte.guardar(reporte);

       Long id = reporte.getId();

       Reporte reporteEsperado = repositorioReporte.buscarReportePorId(id);

       assertThat(reporteEsperado, is(notNullValue()));
       assertThat(reporteEsperado.getDescripcion(), equalTo("Descripcion de prueba"));
   }
    @Test
    public void dadoQueIntentaGuardarUnReporteEnLaBaseYLoHaceCorrectamente() {

        Reporte reporte = new Reporte();
        reporte.setDescripcion("Descripcion de prueba");

        repositorioReporte.guardar(reporte);

        assertThat(reporte, is(notNullValue()));
        assertThat(reporte.getDescripcion(), equalTo("Descripcion de prueba"));
    }

    @Test
    public void dadoQueIntentaEliminarUnReporteYLoHaceCorrectamente() {

        Reporte reporte = new Reporte();
        reporte.setDescripcion("Descripcion de prueba");
        repositorioReporte.guardar(reporte);

        Long idReporte = reporte.getId();

        repositorioReporte.eliminar(reporte);

        Reporte reporteEliminado = repositorioReporte.buscarReportePorId(idReporte);

        assertThat(reporteEliminado, is(nullValue()));
    }

    @Test
    public void dadoQueIntentaObtenerTodosLosReportesDeUnClubYLoHaceCorrectamente() {
        Club club = new Club();
        club.setNombre("Club de Prueba");
        repositorioClub.guardar(club);

        Reporte reporte = new Reporte();
        reporte.setClub(club);
        reporte.setDescripcion("Descripcion de prueba");

        Reporte otroReporte = new Reporte();
        otroReporte.setClub(club);
        otroReporte.setDescripcion("Descripcion");

        repositorioReporte.guardar(reporte);
        repositorioReporte.guardar(otroReporte);

       // List<Reporte> reportesEsperados = repositorioReporte.obtenerTodosLosReportesDeUnClub(club);

        //assertThat(reportesEsperados.size(), is(2));
    }

}