package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioNotificacion;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ControladorUsuarioTest {

    //Mockear el servicio
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioNotificacion servicioNotificacionMock;
    private ControladorUsuario controladorUsuario;
    private Usuario usuarioMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class); //mockeo el request
        sessionMock = mock(HttpSession.class); //aca mockeo la sesion
        servicioUsuarioMock = mock(ServicioUsuario.class); //mockeo el servicio
        servicioNotificacionMock = mock(ServicioNotificacion.class);
        controladorUsuario = new ControladorUsuario(servicioUsuarioMock, servicioNotificacionMock); //le paso al controlador el servicio mockeado
        usuarioMock = mock(Usuario.class); //Mockeo un usuario
    }


    @Test
    public void dadoElMetodoIrAPerfilFuncioneCorrectamenteDebeDevolvermeALaVistaPerfilDelUsuarioEspecifico() throws NoExisteEseUsuario {
        // mock de un usuario
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);

        // mock para usuarioActual (el usuario de sesión)
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioMock.getId())).thenReturn(usuarioMock);

        // mock para el usuario cuyo perfil se muestra
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(usuarioMock);

        ModelAndView model = controladorUsuario.irAPerfil(1L, requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("perfil"));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
    }

//    @Test
//    public void dadoQueElMetodoIrAPerfilSiUnUsuarioNoLogueadoIntentaAccederAUnPerfilLeMuestraLaVistaDelPerfil() throws NoExisteEseUsuario {
//
//        Usuario usuarioPerfil = new Usuario();
//
//
//        when(requestMock.getSession()).thenReturn(sessionMock);
//        when(sessionMock.getAttribute("usuario")).thenReturn(null);
//        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(usuarioPerfil);
//
//        ModelAndView model = controladorUsuario.irAPerfil(2L,requestMock);
//
//        assertThat(model.getViewName(), equalToIgnoringCase("perfil"));
//        assertThat(model.getModel().get("usuario"), equalTo(null));
//    }

    @Test
    public void dadoElMetodoLogoutLaSessionDebeSerPasarASerInvalidadaYDebeDevolvermeAlaVistaHome() {
        when(requestMock.getSession()).thenReturn(sessionMock);

        ModelAndView model = controladorUsuario.logout(requestMock); //uso el metodo logout con el request mock

        verify(sessionMock, times(1)).invalidate(); //verifica que se uso el metodo invalidate
        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/home")); //verifica la vista a la
        // que fuiste redireccionado
    }


    @Test
    public void dadoQueElMetodoIrAMisClubsDebeDevolvermeALaVistaMisClubsConUnModeloQueContengaUnUsuario() throws NoExisteEseUsuario {
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(usuarioMock);

        ModelAndView model = controladorUsuario.irAMisClubs(1L);

        assertThat(model.getViewName(), equalToIgnoringCase("misClubs"));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
    }

    @Test
    public void dadoElMetodoSeguirUsuarioCuandoQuieroUsarloSinEstarLogueadoMeRedireccionaALaVistaLogin() throws NoExisteEseUsuario {
        Long idUsuarioASeguir = 1L;
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        String vista = controladorUsuario.seguirUsuario(idUsuarioASeguir, requestMock);

        assertThat(vista, equalTo("redirect:/login"));
    }

    @Test
    public void dadoElMetodoSeguirUsuarioCuandoQuieroSeguirAUnUsuarioQueNoExisteMeRedireccionaAlHome() throws NoExisteEseUsuario {
        Long idUsuarioASeguir = 1L;
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(new Usuario());
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioMock.getId())).thenReturn(null);

        String vista = controladorUsuario.seguirUsuario(idUsuarioASeguir, requestMock);

        assertThat(vista, equalTo("redirect:/home"));
        verify(servicioUsuarioMock,times(2)).buscarUsuarioPor(any());
    }

    @Test
    public void dadoElMetodoSeguirUsuarioCuandoSigoAUnUsuarioQueExisteMeRedireccionaALaVistaDeSuPerfil() throws NoExisteEseUsuario {
        Usuario usuarioASeguir = new Usuario();
        usuarioASeguir.setId(1L);
        Usuario usuarioSeguidor = new Usuario();
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioASeguir.getId())).thenReturn(usuarioASeguir);
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioMock.getId())).thenReturn(usuarioSeguidor);

        String vista = controladorUsuario.seguirUsuario(usuarioASeguir.getId(), requestMock);

        assertThat(vista, equalTo("redirect:/perfil/" + usuarioASeguir.getId()));
        verify(servicioUsuarioMock,times(2)).buscarUsuarioPor(any());
        verify(servicioUsuarioMock,times(1)).seguirUsuario(usuarioASeguir,usuarioSeguidor);
    }

    @Test
    public void dadoElMetodoDejarDeSeguirUsuarioCuandoQuieroUsarloSinEstarLogueadoMeRedireccionaALaVistaLogin() throws NoExisteEseUsuario {
        Long idUsuarioASeguir = 1L;
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        String vista = controladorUsuario.dejarDeSeguirUsuario(idUsuarioASeguir, requestMock);

        assertThat(vista, equalTo("redirect:/login"));
    }

    @Test
    public void dadoElMetodoDejarDeSeguirUsuarioCuandoQuieroSeguirAUnUsuarioQueNoExisteMeRedireccionaAlHome() throws NoExisteEseUsuario {
        Long idUsuarioASeguir = 1L;
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(new Usuario());
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioMock.getId())).thenReturn(null);

        String vista = controladorUsuario.dejarDeSeguirUsuario(idUsuarioASeguir, requestMock);

        assertThat(vista, equalTo("redirect:/home"));
        verify(servicioUsuarioMock,times(2)).buscarUsuarioPor(any());
    }

    @Test
    public void dadoElMetodoDejarDeSeguirUsuarioCuandoSigoAUnUsuarioQueExisteMeRedireccionaALaVistaDeSuPerfil() throws NoExisteEseUsuario {
        Usuario usuarioASeguir = new Usuario();
        usuarioASeguir.setId(1L);
        Usuario usuarioSeguidor = new Usuario();
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioASeguir.getId())).thenReturn(usuarioASeguir);
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioMock.getId())).thenReturn(usuarioSeguidor);

        String vista = controladorUsuario.dejarDeSeguirUsuario(usuarioASeguir.getId(), requestMock);

        assertThat(vista, equalTo("redirect:/perfil/" + usuarioASeguir.getId()));
        verify(servicioUsuarioMock,times(2)).buscarUsuarioPor(any());
        verify(servicioUsuarioMock,times(1)).dejarDeSeguirUsuario(usuarioASeguir,usuarioSeguidor);
    }


}
