package com.tallerwebi.infraestructura;


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

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class RepositorioUsuarioImpTest {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        this.repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

    @Test
    public void guardarUsuarioDeberiaAgregarloALaBaseDeDatos() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("1234");

        repositorioUsuario.guardar(usuario);

        Usuario usuarioGuardado = repositorioUsuario.buscar("test@example.com");
        assertThat(usuarioGuardado, is(notNullValue()));
        assertThat(usuarioGuardado.getEmail(), equalTo("test@example.com"));
    }

    @Test
    public void buscarUsuarioPorEmailYPassword_deberiaDevolverUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("1234");
        repositorioUsuario.guardar(usuario);

        Usuario resultado = repositorioUsuario.buscarUsuario("test@example.com", "1234");
        assertThat(resultado, is(notNullValue()));
        assertThat(resultado.getEmail(), equalTo("test@example.com"));
    }

    @Test
    public void buscarUsuarioPorEmail_deberiaDevolverUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("1234");
        repositorioUsuario.guardar(usuario);

        Usuario resultado = repositorioUsuario.buscar("test@example.com");
        assertThat(resultado, is(notNullValue()));
        assertThat(resultado.getEmail(), equalTo("test@example.com"));
    }

    @Test
    public void buscarUsuarioPorEmailInexistente_deberiaDevolverNull() {
        Usuario resultado = repositorioUsuario.buscar("ejemplo@example.com");
        assertThat(resultado, is(nullValue()));
    }

    @Test
    public void modificarUsuario_deberiaActualizarLaInformacionDelUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("1234");
        repositorioUsuario.guardar(usuario);

        usuario.setPassword("5678");
        repositorioUsuario.modificar(usuario);

        Usuario usuarioModificado = repositorioUsuario.buscar("test@example.com");
        assertThat(usuarioModificado.getPassword(), equalTo("5678"));
    }

    @Test
    public void buscarTodosLosUsuarios_deberiaDevolverListaDeUsuarios() {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("user1@example.com");
        usuario1.setPassword("1234");
        repositorioUsuario.guardar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("user2@example.com");
        usuario2.setPassword("5678");
        repositorioUsuario.guardar(usuario2);

        List<Usuario> usuarios = repositorioUsuario.buscarTodosLosUsuarios();
        assertThat(usuarios, hasSize(2));
        assertThat(usuarios, containsInAnyOrder(usuario1, usuario2));
    }
    @Test
    public void buscarUsuarioPorId_deberiaDevolverUsuarioCorrecto() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("1234");
        repositorioUsuario.guardar(usuario);

        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuarioPor(usuario.getId());
        assertThat(usuarioEncontrado, is(notNullValue()));
        assertThat(usuarioEncontrado.getEmail(), equalTo("test@example.com"));
    }


}
