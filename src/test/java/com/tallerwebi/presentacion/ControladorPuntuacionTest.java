package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ControladorPuntuacionTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Usuario usuarioMock;
    private ServicioClub servicioClubMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioPuntuacion servicioPuntuacionMock;
    private ControladorPuntuacion controladorPuntuacion;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        servicioClubMock = mock(ServicioClub.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioPuntuacionMock = mock(ServicioPuntuacion.class);
        controladorPuntuacion = new ControladorPuntuacion(servicioUsuarioMock,servicioClubMock,servicioPuntuacionMock);
    }

    @Test
    public void dadoUnControladorPuntuacionCuandoUnClubTiene1SolaVotacionSuPromedioEs0() throws NoExisteEseUsuario, NoExisteEseClub {
        Long idClub = 1L;
        Integer puntuacion = 5;
        Club club = new Club();
        club.setId(1L);
        when(usuarioMock.getId()).thenReturn(1L);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioMock.getId())).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(club.getId())).thenReturn(club);
        when(servicioPuntuacionMock.obtenerPuntuacionPromedio(club)).thenReturn(0.0);

        String vista = controladorPuntuacion.puntuarClub(idClub, puntuacion, requestMock);

        verify(servicioUsuarioMock,times(1)).buscarUsuarioPor(1L);
        verify(servicioClubMock,times(1)).buscarClubPor(1L);
        verify(servicioPuntuacionMock,times(1)).agregarPuntuacion(club, usuarioMock,puntuacion);
        verify(servicioPuntuacionMock,times(0)).obtenerPuntuacionPromedio(club);
        verify(servicioPuntuacionMock,times(0)).actualizarPromedio(club);
        assertThat(vista, equalTo("redirect:/club/" + idClub));
    }

    @Test
    public void dadoUnControladorPuntuacionCuandoUnClubTiene3VotacionesSeCalculaSuPromedio() throws NoExisteEseUsuario, NoExisteEseClub {
        Long idClub = 1L;
        Integer puntuacion = 5;
        Club club = new Club();
        club.setId(1L);
        club.setPuntuaciones(new ArrayList<>());

        club.getPuntuaciones().add(new Puntuacion());
        club.getPuntuaciones().add(new Puntuacion());
        club.getPuntuaciones().add(new Puntuacion());

        when(usuarioMock.getId()).thenReturn(1L);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioMock.getId())).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(club.getId())).thenReturn(club);
        when(servicioPuntuacionMock.obtenerPuntuacionPromedio(club)).thenReturn(4.0);

        String vista = controladorPuntuacion.puntuarClub(idClub, puntuacion, requestMock);

        verify(servicioUsuarioMock,times(1)).buscarUsuarioPor(1L);
        verify(servicioClubMock,times(1)).buscarClubPor(1L);
        verify(servicioPuntuacionMock,times(1)).agregarPuntuacion(club, usuarioMock,puntuacion);
        verify(servicioPuntuacionMock,times(1)).obtenerPuntuacionPromedio(club);
        verify(servicioPuntuacionMock,times(1)).actualizarPromedio(club);
        assertThat(vista, equalTo("redirect:/club/" + idClub));
    }


    @Test
    public void dadoElMetodoDespuntuarRemueveLaPuntuacionYRedirigeALaVistaDelClub() throws NoExisteEseUsuario, NoExisteEseClub {
        Club club = new Club();
        Puntuacion puntuacion = new Puntuacion();
        club.getPuntuaciones().add(puntuacion);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(any())).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(club);

        String vista = controladorPuntuacion.despuntuarClub(1L,requestMock);

        verify(servicioPuntuacionMock,times(1)).removerPuntuacion(club,usuarioMock);
        verify(servicioUsuarioMock,times(1)).buscarUsuarioPor(usuarioMock.getId());
        verify(servicioClubMock,times(1)).buscarClubPor(1L);
        assertThat(vista, equalTo("redirect:/club/1"));
    }
}
