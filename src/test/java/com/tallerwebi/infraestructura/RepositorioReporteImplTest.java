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

   /* CORREGIR
   @Test
   public void dadoElMetodobuscarReportePorIdSiEncuentraElReporteMeLoDevuelve() {
       Club club = new Club();
       club.setNombre("Club de Prueba");
       repositorioClub.guardar(club); // Guarda el club antes de usarlo en el reporte

       Reporte reporte = new Reporte();
       reporte.setId(1L);
       reporte.setClub(club); // Asigna el club guardado al reporte
       reporte.setDescripcion("Descripcion de prueba");

       repositorioReporte.guardar(reporte);

       Reporte reporteEsperado = repositorioReporte.buscarReportePorId(reporte.getId());

       assertThat(reporteEsperado, is(notNullValue()));
       assertThat(reporteEsperado.getDescripcion(), equalTo("Descripcion de prueba"));
   }
   */
}