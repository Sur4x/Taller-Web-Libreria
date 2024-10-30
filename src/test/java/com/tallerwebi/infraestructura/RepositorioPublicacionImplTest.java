package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import javax.transaction.Transactional;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
@Transactional
public class RepositorioPublicacionImplTest {

    private RepositorioPublicacionImpl repositorioPublicacion;
    private RepositorioClubImpl repositorioClub;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        repositorioPublicacion = new RepositorioPublicacionImpl(sessionFactory);
        repositorioClub = new RepositorioClubImpl(sessionFactory);
    }

    @Rollback
    @Test
    public void eliminarPublicacion() {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Reporte 1");
        publicacion.setMensaje("Este es un mssj de reporte");
        repositorioPublicacion.guardar(publicacion);
        repositorioPublicacion.eliminar(publicacion);
        Publicacion resultado = repositorioPublicacion.buscarPublicacionPorId(publicacion.getId());
        assertThat(resultado, is(nullValue()));
    }

    @Rollback
    @Test
    public void dadoElMetodoBuscarPublicacionEnUnClubSiLaEncuentraRetornaUnaPublicacion() {
        Club club = new Club();
        repositorioClub.guardar(club);
        club.setPublicaciones(new ArrayList<>());

        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        publicacion.setClub(club);
        club.getPublicaciones().add(publicacion);

        repositorioPublicacion.guardar(publicacion);

        Publicacion publicacionObtenida = repositorioPublicacion.buscarPublicacionEnUnClub(publicacion.getId(), club);
        assertThat(publicacionObtenida, equalTo(publicacion));
    }

    @Rollback
    @Test
    public void dadoElMetodoBuscarPublicacionEnUnClubSiNoLaEncuentraRetornaNull() {
        Club club = new Club();
        repositorioClub.guardar(club);

        Publicacion publicacionObtenida = repositorioPublicacion.buscarPublicacionEnUnClub(1L, club);
        assertThat(publicacionObtenida, equalTo(null));
    }
}
