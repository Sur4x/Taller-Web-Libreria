package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.RepositorioUsuario;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import javax.transaction.Transactional;
import java.util.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
@Transactional
public class RepositorioPublicacionImplTest {

    private RepositorioPublicacionImpl repositorioPublicacion;
    private RepositorioClubImpl repositorioClub;
    private RepositorioUsuarioImpl repositorioUsuario;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        repositorioPublicacion = new RepositorioPublicacionImpl(sessionFactory);
        repositorioClub = new RepositorioClubImpl(sessionFactory);
        repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
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
        club.setPublicaciones(new ArrayList<Publicacion>());

        Publicacion publicacion = new Publicacion();
        publicacion.setClub(club);
        repositorioPublicacion.guardar(publicacion);
        club.getPublicaciones().add(publicacion);

        repositorioClub.guardar(club);

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

    @Test
    public void testBuscarPublicacionesMasRecientesDeUsuariosSeguidos() {
        Usuario usuario1 = new Usuario();
        usuario1.setNombreUsuario("Usuario1");
        repositorioUsuario.guardar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNombreUsuario("Usuario2");
        repositorioUsuario.guardar(usuario2);

        Publicacion publicacion1 = new Publicacion();
        publicacion1.setUsuario(usuario1);
        publicacion1.setTitulo("Publicación 1");
        repositorioPublicacion.guardar(publicacion1);

        Publicacion publicacion2 = new Publicacion();
        publicacion2.setUsuario(usuario2);
        publicacion2.setTitulo("Publicación 2");
        repositorioPublicacion.guardar(publicacion2);

        Publicacion publicacion3 = new Publicacion();
        publicacion3.setUsuario(usuario1);
        publicacion3.setTitulo("Publicación 3");
        repositorioPublicacion.guardar(publicacion3);

        Set<Usuario> usuariosSeguidos = new HashSet<>(Arrays.asList(usuario1, usuario2));

        List<Long> usuariosSeguidosIds = new ArrayList<>();
        for (Usuario usuario : usuariosSeguidos) {
            usuariosSeguidosIds.add(usuario.getId());
        }

        String hql = "FROM Publicacion p WHERE p.usuario.id IN :usuariosSeguidos ORDER BY p.id DESC";
        List<Publicacion> publicaciones = this.sessionFactory.getCurrentSession().createQuery(hql, Publicacion.class)
                .setParameter("usuariosSeguidos", usuariosSeguidosIds)
                .setMaxResults(5)
                .getResultList();

        assertThat(publicaciones.size(), equalTo(3));
        assertThat(publicaciones.get(0).getTitulo(), equalTo("Publicación 3")); // Más reciente
        assertThat(publicaciones.get(1).getTitulo(), equalTo("Publicación 2"));
        assertThat(publicaciones.get(2).getTitulo(), equalTo("Publicación 1"));
    }

}
