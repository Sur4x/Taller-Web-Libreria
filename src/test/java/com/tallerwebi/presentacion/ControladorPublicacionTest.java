package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorPublicacionTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Usuario usuarioMock;
    private ControladorPublicacion controladorPublicacion;
    private ServicioPublicacion servicioPublicacionMock;
    private ServicioClub servicioClubMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioPuntuacion servicioPuntuacionMock;
    private Club clubMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        this.servicioPublicacionMock = mock(ServicioPublicacionImpl.class);
        this.servicioClubMock = mock(ServicioClubImpl.class);
        this.servicioUsuarioMock = mock(ServicioUsuarioImpl.class);
        this.servicioPuntuacionMock = mock(ServicioPuntuacionImp.class);
        controladorPublicacion = new ControladorPublicacion(servicioPublicacionMock,servicioClubMock,servicioUsuarioMock,servicioPuntuacionMock);
        clubMock = mock(Club.class);
    }

    @Test
    public void dadoElMetodoIrANuevaPublicacionFuncionaCorretamenteMeDirijeANuevaPublicacion() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        ModelAndView modelo = controladorPublicacion.irANuevaPublicacion(1L, requestMock);
        assertThat(modelo.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(modelo.getViewName(), equalToIgnoringCase("nuevaPublicacion"));
    }

    @Test
    public void dadoElMetodoIrANuevaPublicacionSiElUsuarioNoEstaLogueadoMeDirijeALogin() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);
        ModelAndView modelo = controladorPublicacion.irANuevaPublicacion(1L, requestMock);
        assertThat(modelo.getModel().get("usuario"), equalTo(null));
        assertThat(modelo.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void dadoElMetodoRealizarPublicacionAlAgregarUnaNuevaPublicacionMeDirijeALogin() throws NoExisteEseClub {
        Club club = new Club();
        club.setId(1L);
        when(requestMock.getSession()).thenReturn(sessionMock);
        Publicacion publicacion = new Publicacion();

        ModelAndView modelo = controladorPublicacion.realizarPublicacion(club.getId(),publicacion, requestMock);

        assertThat(modelo.getViewName(), equalToIgnoringCase("detalleClub"));
    }

    @Test
    public void dadoElMetodoEliminarPublicacionCuandoSoyAdminPuedoEliminarLaPublicacionYMeRedirijeALaVistaEspecificaDelClub() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Club club = new Club();
        when(servicioClubMock.buscarClubPor(any())).thenReturn(club);
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(servicioPublicacionMock.buscarPublicacionPorId(publicacion.getId())).thenReturn(publicacion);
        when(servicioUsuarioMock.esAdmin(usuarioMock)).thenReturn(true);

        ModelAndView modelo = controladorPublicacion.eliminarPublicacion(club.getId(), publicacion.getId(),requestMock);

        assertThat(modelo.getViewName(), equalToIgnoringCase("redirect:/club/{clubId}"));
    }

    @Test
    public void dadoElMetodoEliminarPublicacionCuandoSoyUsuarioNormalNoPuedoEliminarLaPublicacionYMeRedirijeALaVistaHome() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Club club = new Club();
        club.setId(1L);
        when(servicioClubMock.buscarClubPor(club.getId())).thenReturn(club);
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(servicioPublicacionMock.buscarPublicacionPorId(publicacion.getId())).thenReturn(publicacion);
        when(servicioUsuarioMock.esAdmin(usuarioMock)).thenReturn(false);

        ModelAndView modelo = controladorPublicacion.eliminarPublicacion(club.getId(), publicacion.getId(),requestMock);

        assertThat(modelo.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void dadoElMetodoIrDetallePublicacionSiExisteLaPublicacionMeDireccionaALaVistaDeEstaPublicacion() throws NoExisteEseClub {
        Comentario comentario = new Comentario();
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        publicacion.setComentarios(new ArrayList<>());
        publicacion.getComentarios().add(comentario);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(publicacion);

        ModelAndView modelo = controladorPublicacion.irAdetallePublicacion(clubMock.getId(), publicacion.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("detallePublicacion"));
        assertThat(modelo.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(modelo.getModel().get("club"), equalTo(clubMock));
        assertThat(modelo.getModel().get("publicacion"), equalTo(publicacion));
        assertThat(modelo.getModel().get("comentarios"), equalTo(publicacion.getComentarios()));
    }

    @Test
    public void dadoElMetodoIrDetallePublicacionSiNOExisteLaPublicacionMeDireccionaALaVistaHome() throws NoExisteEseClub {
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(null);

        ModelAndView modelo = controladorPublicacion.irAdetallePublicacion(clubMock.getId(), publicacion.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/home"));
    }
}
