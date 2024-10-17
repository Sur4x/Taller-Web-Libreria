package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

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
        clubMock = mock(Club.class);


    }

    @Test
    public void dadoElMetodoIrACrearNuevoClubCuandoEstoyLogueadoDebeDevolvermeLaVistaCrearClub() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);

        ModelAndView model = controladorClub.irACrearNuevoClub(requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("crearClub"));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(model.getModel().get("club") instanceof Club, equalTo(true));
    }

    @Test
    public void dadoElMetodoIrACrearNuevoClubCuandoNoEstoyLogueadoDebeDevolvermeLaVistaCrearClub() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView model = controladorClub.irACrearNuevoClub(requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void dadoElMetodoCrearNuevoClubCuandoSeCreaCorrectamenteMeRedirireccionaAlaVistaDelClubEspecifico() throws ClubExistente, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.agregar(any(Club.class))).thenReturn(true);

        ModelAndView model = controladorClub.crearNuevoClub(new Club(), requestMock);
        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/club/{clubId}"));

    }

    @Test
    public void dadoElMetodoCrearNuevoClubCuandoNoSeCreaCorrectamenteMeRedirireccionaAlaVistaHome() throws ClubExistente, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.agregar(any(Club.class))).thenReturn(false);

        ModelAndView model = controladorClub.crearNuevoClub( new Club(), requestMock);
        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void dadoElMetodoIrADetalleClubSiElClubExisteDebeRedireccionarmeALaVistaDetalleClub() throws NoExisteEseClub {
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
    public void dadoElMetodoIrADetalleClubSiElClubNOExisteDebeRedireccionarmeALaVistaDetalleClub() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(1L)).thenReturn(null);
        ModelAndView model = controladorClub.irADetalleClub(1L, requestMock);
        assertThat(model.getViewName(), equalToIgnoringCase("Redirect: /home"));
    }

    @Test
    public void dadoElMetodoIrANuevaPublicacionFuncionaCorretamenteMeDirijeANuevaPublicacion() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        ModelAndView modelo = controladorClub.irANuevaPublicacion(1L, requestMock);
        assertThat(modelo.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(modelo.getViewName(), equalToIgnoringCase("nuevaPublicacion"));
    }
