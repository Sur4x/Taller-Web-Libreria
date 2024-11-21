
package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
@Transactional
public class RepositorioNotificacionImplTest {

    private RepositorioNotificacionImpl repositorioNotificacion;
    private RepositorioUsuarioImpl repositorioUsuario;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        repositorioNotificacion = new RepositorioNotificacionImpl(sessionFactory);
        repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

    @Test
    @Rollback
    public void dadoQueIntentoGuardarUnaNotificacionYLoHaceCorrectamente(){
        Usuario usuario = new Usuario();
        repositorioUsuario.guardar(usuario);

        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setEvento("Evento test");
        notificacion.setUsuario(usuario);

        repositorioNotificacion.guardar(notificacion);

        String hql = "FROM Notificacion n WHERE n.id= :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", notificacion.getId());
        Notificacion notificacionEncontrada = (Notificacion) query.getSingleResult();

        assertThat(notificacionEncontrada, equalTo(notificacion));
    }

    @Test
    @Rollback
    public void dadoQueIntentoBuscarUnaNotificacionPorIdYLoHaceCorrectamente(){
        Long id = 1L;
        Notificacion notificacion = new Notificacion();
        notificacion.setId(id);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setEvento("Evento test");

        repositorioNotificacion.buscarNotificacionPor(id);

        assertThat(notificacion, is(notNullValue()));
    }

    @Test
    @Rollback
    public void dadoQueElMetodoListarTodasLasNotificacionesDeUnUsuarioLoHaceCorrectamente(){
        Usuario usuario = new Usuario();
        repositorioUsuario.guardar(usuario);
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setEvento("Evento test");

        Notificacion otraNotificacion = new Notificacion();
        otraNotificacion.setFecha(LocalDateTime.now());
        otraNotificacion.setEvento("Evento test 2");

        usuario.getNotificaciones().add(notificacion);
        usuario.getNotificaciones().add(otraNotificacion);

        notificacion.setUsuario(usuario);
        otraNotificacion.setUsuario(usuario);

        repositorioNotificacion.guardar(notificacion);
        repositorioNotificacion.guardar(otraNotificacion);

        List<Notificacion> lista = repositorioNotificacion.listarTodasLasNotificacionesDeUnUsuario(usuario.getId());

        assertThat(lista.size(), is(2));
    }

}


