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
import static org.mockito.Mockito.*;

public class ControladorLikeTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ControladorLike controladorLike;
    private ServicioLike servicioLikeMock;
    private ServicioPublicacion servicioPublicacionMock;
    private ServicioClub servicioClubMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioPublicacionMock = mock(ServicioPublicacion.class);
        servicioClubMock = mock(ServicioClub.class);
        servicioLikeMock = mock(ServicioLike.class);
        controladorLike = new ControladorLike(servicioLikeMock,servicioPublicacionMock,servicioClubMock);
    }

    @Test
    public void dadoUnControladorCuandoDoyLikeCorrectamenteSeAgregaElLikeConUnMensajeExitoso() throws NoExisteEseClub {
        Usuario usuario = new Usuario();
        usuario.setEmail("asd");
        Comentario comentario = new Comentario();
        comentario.setAutor(usuario);
        Club club = new Club();
        club.setNombre("clubsito");
        Publicacion publicacion = new Publicacion();
        publicacion.setMensaje("Asd");

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(servicioLikeMock.agregarLike(comentario.getId(), usuario)).thenReturn(true);
        when(servicioPublicacionMock.buscarPublicacionPorId(publicacion.getId())).thenReturn(publicacion);

        ModelAndView model = controladorLike.darLike(comentario.getId(), club.getId(),publicacion.getId(),requestMock);

        verify(servicioLikeMock,times(1)).agregarLike(comentario.getId(),usuario);//se verifico que el metodo ando auque sea una vez
        verify(servicioPublicacionMock,times(1)).buscarPublicacionPorId(publicacion.getId());
        verify(servicioClubMock,times(1)).buscarClubPor(club.getId());
        assertThat(model.getViewName(),equalTo("detallePublicacion"));
        assertThat(model.getModel().get("mensaje"),equalTo("Like agregado correctamente."));
    }

    @Test
    public void dadoUnControladorLikeCuandoDoyLikeCorrectamenteSeUtilizaElMetodoAgregarLikeYMeLLevaAlaVistaDetallePublicacion() throws NoExisteEseClub {
        Usuario usuario = new Usuario();
        usuario.setEmail("asd");
        Comentario comentario = new Comentario();
        comentario.setAutor(usuario);
        Club club = new Club();
        club.setNombre("clubsito");
        Publicacion publicacion = new Publicacion();
        publicacion.setMensaje("Asd");

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(servicioLikeMock.agregarLike(comentario.getId(), usuario)).thenReturn(false);
        when(servicioPublicacionMock.buscarPublicacionPorId(publicacion.getId())).thenReturn(null);

        ModelAndView model = controladorLike.darLike(comentario.getId(), club.getId(),publicacion.getId(),requestMock);

        verify(servicioLikeMock,times(1)).agregarLike(comentario.getId(),usuario);
        verify(servicioPublicacionMock,times(1)).buscarPublicacionPorId(publicacion.getId());
        verify(servicioClubMock,times(0)).buscarClubPor(club.getId());
        assertThat(model.getViewName(),equalTo("detallePublicacion"));
        assertThat(model.getModel().get("mensaje"),equalTo("Problemas al agregar el like."));
    }
}
