package com.tallerwebi.presentacion;

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
    private ControladorUsuario controladorUsuario;
    private Usuario usuarioMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class); //mockeo el request
        sessionMock = mock(HttpSession.class); //aca mockeo la sesion
        servicioUsuarioMock = mock(ServicioUsuario.class); //mockeo el servicio
        controladorUsuario = new ControladorUsuario(servicioUsuarioMock); //le paso al controlador el servicio mockeado
        usuarioMock = mock(Usuario.class); //Mockeo un usuario
    }


    @Test
    public void dadoElMetodoIrAPerfilFuncioneCorrectamenteDebeDevolvermeALaVistaPerfilDelUsuarioEspecifico() throws NoExisteEseUsuario {
        //mock de un usuario
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(usuarioMock);

        ModelAndView model = controladorUsuario.irAPerfil(1L);

        assertThat(model.getViewName(), equalToIgnoringCase("perfil"));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
    }

    @Test
    public void dadoQueElMetodoIrAPerfilIntentaAccederAUnPerfilQueNoEsElDelUsuarioLogueadoRedireccionaALaVistaLogin() throws NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        Usuario usuario = new Usuario();
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(usuario);

        ModelAndView model = controladorUsuario.irAPerfil(1L);

        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/login"));
        assertThat(model.getModel().get("usuario"), equalTo(null));
    }

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


}
