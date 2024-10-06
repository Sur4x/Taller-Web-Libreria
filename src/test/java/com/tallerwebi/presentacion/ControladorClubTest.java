package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.ServicioClub;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorClubTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioClub servicioClubMock;
    private ServicioUsuario servicioUsuarioMock;
    private Club clubMock;
    private ControladorClub controladorClub;
    private Usuario usuarioMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class); //mockeo el request
        sessionMock = mock(HttpSession.class); //aca mockeo la sesion
        servicioClubMock = mock(ServicioClub.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        clubMock = mock(Club.class);
        controladorClub = new ControladorClub(servicioClubMock, servicioUsuarioMock);
        usuarioMock = mock(Usuario.class);
    }

    @Test
    public void DadoCuandoUtilizoElMetodoIrACrearNuevoClubMeDireccionaALaVistaCrearClubConUnUsuarioYUnClubEnElModelo(){

        when(requestMock.getSession()).thenReturn(sessionMock); //primero mockeo la sesion
        when(requestMock.getSession().getAttribute("usuario")).thenReturn(usuarioMock); //despues moqueo el getAttribute

        ModelAndView model = controladorClub.irACrearNuevoClub(requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("crearClub"));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(model.getModel().get("club"), equalTo(new Club()));

    }

    @Test
    public void DadoCuandoUtilizoElMetodoIrACrearNuevoClubMeDireccionaALaVistaLoginConUnUsuarioNull(){

        when(requestMock.getSession()).thenReturn(sessionMock); //primero mockeo la sesion
        when(requestMock.getSession().getAttribute("usuario")).thenReturn(null); //despues moqueo el getAttribute

        ModelAndView model = controladorClub.irACrearNuevoClub(requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("Redirect:/login"));
        assertThat(model.getModel().get("usuario"), equalTo(null));

    }

}
