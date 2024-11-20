package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
@Transactional
public class RepositorioNotificacionImplTest {

    private RepositorioNotificacionImpl repositorioNotificacion;
    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        repositorioNotificacion = new RepositorioNotificacionImpl(sessionFactory);
    }

    @Test
    public void dadoQueIntentoGuardarUnaNotificacionYLoHaceCorrectamente(){
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDate.now());
        notificacion.setEvento("Evento test");

        repositorioNotificacion.crearNotificacion(notificacion);

        assertThat(notificacion, is(notNullValue()));
    }

    @Test
    public void dadoQueIntentoBuscarUnaNotificacionPorIdYLoHaceCorrectamente(){
        Long id = 1L;
        Notificacion notificacion = new Notificacion();
        notificacion.setId(id);
        notificacion.setFecha(LocalDate.now());
        notificacion.setEvento("Evento test");

        repositorioNotificacion.buscarNotificacionPor(id);

        assertThat(notificacion, is(notNullValue()));
    }

    @Test
    public void dadoQueIntentoObtenerUnaListaDeNotificacionesYLoHaceCorrectamente(){
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDate.now());
        notificacion.setEvento("Evento test");

        Notificacion otraNotificacion = new Notificacion();
        otraNotificacion.setFecha(LocalDate.now());
        otraNotificacion.setEvento("Evento test 2");

        repositorioNotificacion.crearNotificacion(notificacion);
        repositorioNotificacion.crearNotificacion(otraNotificacion);

        List<Notificacion> lista = repositorioNotificacion.listarTodasLasNotificaciones();

        assertThat(lista.size(), is(2));
    }
}
