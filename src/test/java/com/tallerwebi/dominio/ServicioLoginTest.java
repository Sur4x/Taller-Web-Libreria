package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServicioLoginTest {
    @Mock
    private RepositorioUsuario repositorioUsuario;
    @InjectMocks
    private ServicioLoginImpl servicioLogin;
    private Usuario usuario;

    @BeforeEach
    public void init() {
        usuario = mock(Usuario.class);
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void dadoElMetodoConsultarUsuarioSiLeEntregoUnUsuarioQueEstaEnElSistemaMeLoDevuelve(){

        Usuario roberto = new Usuario();
        roberto.setEmail("roberto@noReply.com.ar");
        roberto.setPassword("1234");

        Mockito.when(repositorioUsuario.buscarUsuario(anyString(),anyString())).thenReturn(roberto);

        Usuario resultado = servicioLogin.consultarUsuario("roberto@noReply.com.ar", "1234");

        assertThat(resultado.getEmail(), equalToIgnoringCase("roberto@noReply.com.ar"));
        assertThat(resultado.getPassword(), equalToIgnoringCase("1234"));
    }

    @Test
    public void dadoElMetodoConsultarUsuarioSiLeEntregoUnUsuarioQueNoEstaEnElSistemaMeDevuelveNull(){

        Usuario roberto = new Usuario();
        roberto.setEmail("roberto@noReply.com.ar");
        roberto.setPassword("1234");

        Mockito.when(repositorioUsuario.buscarUsuario(anyString(),anyString())).thenReturn(null);

        Usuario resultado = servicioLogin.consultarUsuario("roberto@noReply.com.ar", "1234");

        assertThat(resultado, equalTo(null));
    }

    @Test
    public void dadoElMetodoRegistrarCuandoIntentoRegistrarAUnUsuarioQueYaExisteLanzaUnaExcepcion(){

        Usuario roberto = new Usuario();
        roberto.setEmail("roberto@noReply.com.ar");
        roberto.setPassword("1234");

        Mockito.when(repositorioUsuario.buscarUsuario(anyString(),anyString())).thenReturn(roberto);

        assertThrows(UsuarioExistente.class, () -> servicioLogin.registrar(roberto));

        verify(repositorioUsuario, never()).guardar(any(Usuario.class));
    }

    @Test
    public void dadoElMetodoRegistrarCuandoIntentoRegistrarAUnUsuarioQueNoExisteEnElSistemaLoRegistra() throws UsuarioExistente {

        Usuario roberto = new Usuario();
        roberto.setEmail("roberto@noReply.com.ar");
        roberto.setNombreUsuario("roberGalatti");
        roberto.setPassword("1234");
        roberto.setConfirmPassword("1234");

        Mockito.when(repositorioUsuario.buscarUsuario(anyString(),anyString())).thenReturn(null);

        servicioLogin.registrar(roberto);

        verify(repositorioUsuario, times(1)).guardar(roberto);
    }
}
