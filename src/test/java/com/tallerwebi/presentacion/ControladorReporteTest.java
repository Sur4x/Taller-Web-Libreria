package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.ReporteExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ControladorReporteTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Usuario usuarioMock;
    private Club clubMock;
    private ServicioClub servicioClubMock;
    private ServicioReporte servicioReporteMock;
    private ControladorReporte controladorReporte;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class); // Mock de un usuario
        clubMock = mock(Club.class);
        servicioClubMock = mock(ServicioClub.class);
        servicioReporteMock = mock(ServicioReporte.class);
        controladorReporte = new ControladorReporte(servicioClubMock, servicioReporteMock);
    }

    @Test
    public void dadoElMetodoMostrarFormularioReporteCuandoQuieroHacerUnReporteDeUnClubExistenteMeRedireccionaALaVistaCrearReporte() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);

        ModelAndView modelo = controladorReporte.mostrarFormularioReporte(clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("crearReporte"));
        assertThat(modelo.getModel().get("club"), equalTo(clubMock));
        assertThat(modelo.getModel().get("usuario"), equalTo(usuarioMock));
    }

    @Test
    public void dadoElMetodoMostrarFormularioReporteCuandoQuieroHacerUnReporteDeUnClubInexistenteMeRedireccionaALaVistaHome() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(null);

        ModelAndView modelo = controladorReporte.mostrarFormularioReporte(clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/login"));
    }

    @Test
    public void dadoElMetodoRealizarNuevoReporteSiExisteElClubAlQueQuieroReportarMeDireccionaALaVistaEspecificaDelClub() throws NoExisteEseClub, ReporteExistente {
        Reporte reporte = new Reporte(); //creo el reporte
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock); //mockeo el usuario
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock); //cuando llamo a buscarClubPor devuelve el club

        ModelAndView modelo = controladorReporte.realizarNuevoReporte(clubMock.getId(), reporte, requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/club/" + clubMock.getId()));
        verify(servicioReporteMock, times(1)).guardarReporte(reporte);
    }

    @Test
    public void dadoElMetodoRealizarNuevoReporteSiNOExisteElClubAlQueQuieroReportarMeDireccionaALaVistaHome() throws NoExisteEseClub, ReporteExistente {
        Reporte reporte = new Reporte();
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(null);

        ModelAndView modelo = controladorReporte.realizarNuevoReporte(clubMock.getId(), reporte, requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/home"));
        verify(servicioReporteMock, times(0)).guardarReporte(reporte);
    }

    @Test
    public void dadoElMetodoListarReportesSiElUsuarioNoExisteRedireccionaALogin() throws Exception {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorReporte.mostrarReportesPorClub(1L, requestMock);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    public void dadoElMetodoMostrarReportesPorClub_ClubNoExiste() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(new Usuario());
        when(servicioClubMock.buscarClubPor(anyLong())).thenReturn(null);

        assertThrows(NoExisteEseClub.class, () -> {
            controladorReporte.mostrarReportesPorClub(1L, requestMock);
        });
    }

    @Test
    public void dadoElMetodoMostrarReportesPorClubSeRealizaConExito() throws Exception {
        Usuario usuario = new Usuario();
        Club club = new Club();
        List<Reporte> reportes = new ArrayList<>();

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(servicioClubMock.buscarClubPor(anyLong())).thenReturn(club);
        when(servicioReporteMock.listarReportesPorClub(club)).thenReturn(reportes);

        ModelAndView modelAndView = controladorReporte.mostrarReportesPorClub(1L, requestMock);

        assertEquals("verReportes", modelAndView.getViewName());
        assertEquals(club, modelAndView.getModel().get("club"));
        assertEquals(usuario, modelAndView.getModel().get("usuario"));
        assertEquals(reportes, modelAndView.getModel().get("reportes"));
    }
}
