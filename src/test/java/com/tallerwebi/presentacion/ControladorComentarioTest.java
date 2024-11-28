package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ControladorComentarioTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Usuario usuarioMock;
    private Club clubMock;
    private ControladorComentario controladorComentario;
    private ServicioPublicacion servicioPublicacionMock;
    private ServicioComentario servicioComentarioMock;
    private ServicioClub servicioClubMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        clubMock = mock(Club.class);
        servicioPublicacionMock = mock(ServicioPublicacionImpl.class);
        servicioComentarioMock = mock(ServicioComentarioImpl.class);
        servicioClubMock = mock(ServicioClubImpl.class);
        controladorComentario = new ControladorComentario(servicioComentarioMock,servicioPublicacionMock,servicioClubMock);
    }

    @Test
    public void dadoElMetodoCrearNuevoComentarioAlmaceneElComentarioCorrectamenteDebeRedireccionarmeALaVistaEspecificaDeLaPublicacion() throws NoExisteEseClub {
        Comentario comentario = new Comentario();
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(servicioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(publicacion);

        ModelAndView modelo = controladorComentario.crearNuevoComentario(comentario, publicacion.getId(), clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("detallePublicacion"));
        verify(servicioComentarioMock, times(1)).guardarComentario(comentario, publicacion);
    }

    @Test
    public void dadoElMetodoCrearNuevoComentarioNoAlmaceneElComentarioCorrectamenteDebeRedireccionarmeALaVistaLogin() throws NoExisteEseClub {
        Comentario comentario = new Comentario();
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(servicioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(null);

        ModelAndView modelo = controladorComentario.crearNuevoComentario(comentario, publicacion.getId(), clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/login"));
        verify(servicioComentarioMock, times(0)).guardarComentario(comentario, publicacion);
    }

    @Test
    public void dadoElMetodoIrAEliminarComentarioSiLoEliminarCorrectamenteMeRedireccionaALaVistaDetallaClub() throws NoExisteEseClub {
        Long clubId = 1L;
        Long publicacionId = 2L;
        Long comentarioId = 3L;
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Comentario comentario = new Comentario();
        comentario.setId(comentarioId);

        Club club = new Club();
        club.setId(clubId);

        Publicacion publicacion = new Publicacion();
        publicacion.setId(publicacionId);

        when(servicioClubMock.buscarClubPor(clubId)).thenReturn(club);
        when(servicioPublicacionMock.buscarPublicacionEnUnClub(publicacionId, club)).thenReturn(publicacion);
        when(servicioComentarioMock.buscarComentarioEnUnaPublicacion(comentarioId, publicacion)).thenReturn(comentario);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);

        ModelAndView modelo = controladorComentario.IrAEliminarComentario(clubId, publicacionId,comentarioId, requestMock);

        assertThat(modelo.getViewName(), equalTo("detallePublicacion"));
        verify(servicioComentarioMock, times(1)).eliminarComentario(comentario);
        verify(servicioPublicacionMock, times(1)).buscarPublicacionEnUnClub(publicacionId, club);
        verify(servicioComentarioMock, times(1)).buscarComentarioEnUnaPublicacion(comentarioId,publicacion);
    }

    @Test
    public void dadoElMetodoIrAEliminarComentarioSiNoLoPuedoEliminarCorrectamenteMeRedireccionaALaVistaDetallaClub() throws NoExisteEseClub {
        Long clubId = 1L;
        Long publicacionId = 2L;
        Long comentarioId = 3L;
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Comentario comentario = new Comentario();
        comentario.setId(comentarioId);

        Club club = new Club();
        club.setId(clubId);

        Publicacion publicacion = new Publicacion();
        publicacion.setId(publicacionId);

        when(servicioClubMock.buscarClubPor(clubId)).thenReturn(club);
        when(servicioPublicacionMock.buscarPublicacionEnUnClub(publicacionId, club)).thenReturn(publicacion);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);

        ModelAndView modelo = controladorComentario.IrAEliminarComentario(clubId, publicacionId,comentarioId, requestMock);

        assertThat(modelo.getViewName(), equalTo("detallePublicacion"));
        verify(servicioComentarioMock, times(0)).eliminarComentario(comentario);
        verify(servicioPublicacionMock, times(1)).buscarPublicacionEnUnClub(publicacionId, club);
    }
}
