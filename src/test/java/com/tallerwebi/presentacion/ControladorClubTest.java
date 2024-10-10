package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorClubTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioClub servicioClubMock;
    private ServicioUsuario servicioUsuarioMock;
    private ControladorClub controladorClub;
    private Usuario usuarioMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class); // Mock del request
        sessionMock = mock(HttpSession.class); // Mock de la sesión
        servicioClubMock = mock(ServicioClub.class); // Mock del servicio de club
        servicioUsuarioMock = mock(ServicioUsuario.class); // Mock del servicio de usuario
        controladorClub = new ControladorClub(servicioClubMock, servicioUsuarioMock); // Controlador con mocks
        usuarioMock = mock(Usuario.class); // Mock de un usuario
    }

    @Test
    public void dadoQueElUsuarioEstaEnSesionIrACrearNuevoClubDebeDevolverVistaCrearClub() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);

        ModelAndView model = controladorClub.irACrearNuevoClub(requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("crearClub"));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(model.getModel().get("club") instanceof Club, equalTo(true));
    }

    @Test
    public void dadoQueElUsuarioNoEstaEnSesionIrACrearNuevoClubDebeRedirigirALogin() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView model = controladorClub.irACrearNuevoClub(requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void dadoQueElClubSeCreaCorrectamenteClubDebeRedirigirAHome() throws ClubExistente, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.agregar(any(Club.class))).thenReturn(true);

        ModelAndView model = controladorClub.crearNuevoClub(new Club(), requestMock);
        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/home"));

    }

    @Test
    public void dadoQueElClubNoSeCreaCorrectamenteCreaNuevoClubDebeRedirigirAHome() throws ClubExistente, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.agregar(any(Club.class))).thenReturn(false);

        ModelAndView model = controladorClub.crearNuevoClub( new Club(), requestMock);
        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void dadoQueElClubExisteIrADetalleClubTieneQueDarElDetalleDelClub() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Club clubMock = mock(Club.class);
        when(servicioClubMock.buscarClubPor(1L)).thenReturn(clubMock);

        ModelAndView model =  controladorClub.irADetalleClub(1L, requestMock);
        assertThat(model.getViewName(), equalToIgnoringCase("detalleClub"));
        assertThat(model.getModel().get("club"), equalTo(clubMock));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
    }
    @Test
    public void dadoQueElClubNoExisteIrADetalleClubRedirigirAlHome() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(1L)).thenReturn(null);
        ModelAndView model = controladorClub.irADetalleClub(1L, requestMock);
        assertThat(model.getViewName(), equalToIgnoringCase("Redirect: /home"));
    }

    @Test
    public void dadoQueElClubExisteEliminarClub ()  throws NoExisteEseClub {
        Long clubId = 1L;
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        doNothing().when(servicioClubMock).eliminarClub(clubId); //do Nothing hace que no haga haga nada cuando llama al servicio
        ModelAndView model = controladorClub.eliminarClub(clubId);
        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/home"));
        verify(servicioClubMock).eliminarClub(clubId);
    }

    }
