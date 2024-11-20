package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.ReporteExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioReporteTest {

    private RepositorioReporte repositorioReporteMock;
    private RepositorioClub repositorioClubMock;
    private ServicioReporte servicioReporte;
    private RepositorioNotificacion repositorioNotificacionMock;

    private Notificacion notificacionMock;

    @BeforeEach
    public void init(){
        repositorioReporteMock = mock(RepositorioReporte.class);
        repositorioClubMock = mock(RepositorioClub.class);
        repositorioNotificacionMock = mock(RepositorioNotificacion.class);
        notificacionMock = mock(Notificacion.class);
        this.servicioReporte = new ServicioReporteImpl(repositorioReporteMock, repositorioClubMock,repositorioNotificacionMock);
    }

    @Test
    public void dadoElMetodoBuscarReportePorIdCuandoLeEntregoUnIdQueExisteEnElSistemaDebeDevolvermeElReporte(){
        Reporte reporte = new Reporte();
        reporte.setId(1L);
        when(repositorioReporteMock.buscarReportePorId(any())).thenReturn(reporte);

        Reporte reporteEncontrado = servicioReporte.buscarReportePorId(1L);

        assertThat(reporteEncontrado.getId(), equalTo(1L));
    }

    @Test
    public void dadoElMetodoBuscarReportePorIdCuandoLeEntregoUnIdQueNOExisteEnElSistemaDebeDevolvermeElReporteVacio(){
        Reporte reporte = new Reporte();
        reporte.setId(1L);
        when(repositorioReporteMock.buscarReportePorId(any())).thenReturn(null);

        Reporte reporteEncontrado = servicioReporte.buscarReportePorId(1L);

        assertThat(reporteEncontrado, equalTo(null));
    }

    @Test
    public void dadoElMetodoGuardarReporteCuandoLeEntregoUnReporteQueNoExisteEnElSistemaEsteDebeRealizarElMetodoGuardar() throws ReporteExistente {
        Reporte reporte = new Reporte();
        Club club = new Club();
        club.setNombre("Club de Lectura");
        reporte.setId(1L);
        reporte.setClub(club);
        when(repositorioReporteMock.buscarReportePorId(any())).thenReturn(null);

        servicioReporte.guardarReporte(reporte);

        verify(repositorioReporteMock, times(1)).guardar(reporte);

        verify(repositorioNotificacionMock).crearNotificacion(argThat(notificacion ->
                notificacion.getFecha().equals(LocalDate.now()) &&
                        notificacion.getEvento().equals("Se realizó un reporte a un club existente: Club de Lectura")
        ));

    }

    @Test
    public void dadoElMetodoGuardarReporteCuandoLeEntregoUnReporteQueSiExisteEnElSistemaLanzaUnaExcepcion() throws ReporteExistente {
        Reporte reporte = new Reporte();
        reporte.setId(1L);
        when(repositorioReporteMock.buscarReportePorId(reporte.getId())).thenReturn(reporte);

        ReporteExistente excepcionEsperada = assertThrows(ReporteExistente.class, () -> servicioReporte.guardarReporte(reporte));

        assertEquals(excepcionEsperada.getMessage(), "El reporte ya fue realizado");
        verify(repositorioReporteMock, times(0)).guardar(reporte);
    }

    @Test
    public void dadoElMetodoEliminarReporteSiLeEntregoUnReporteQueEstaEnElSistemaDebeRealizarElMetodoEliminar(){
        Reporte reporte = new Reporte();
        Club club = new Club();
        club.setNombre("Club de Lectura");
        reporte.setId(1L);
        reporte.setClub(club);
        reporte.setMotivo("Testing");
        when(repositorioReporteMock.buscarReportePorId(any())).thenReturn(reporte);

        servicioReporte.eliminarReporte(1L);

        verify(repositorioReporteMock,times(1)).eliminar(reporte);

        verify(repositorioNotificacionMock).crearNotificacion(argThat(notificacion ->
                notificacion.getFecha().equals(LocalDate.now()) &&
                        notificacion.getEvento().equals("Se elimino un reporte realizado al club: Club de Lectura que tenia como motivo: Testing")
        ));
    }

    @Test
    public void dadoElMetodoEliminarReporteSiLeEntregoUnReporteQueNoEstaEnElSistemaDebeNoRealizaElMetodoEliminar(){
        Reporte reporte = new Reporte();
        when(repositorioReporteMock.buscarReportePorId(any())).thenReturn(null);

        servicioReporte.eliminarReporte(1L);

        verify(repositorioReporteMock,times(0)).eliminar(reporte);
    }

    @Test
    public void dadoElMetodoListarReportesPorClubObtengoUnaListaConReportes() throws NoExisteEseClub {
        Club club = new Club();

        Reporte reporte = new Reporte();
        reporte.setClub(club);
        Reporte otroReporte = new Reporte();
        otroReporte.setClub(club);

        List<Reporte> lista = new ArrayList<>();
        lista.add(reporte);
        lista.add(otroReporte);

        when(repositorioReporteMock.obtenerTodosLosReportesDeUnClub(any())).thenReturn(lista);

        List<Reporte> resultado = servicioReporte.listarReportesPorClub(club);

        assertThat(resultado.size(), is(2));
    }

    @Test
    public void dadoElMetodoObtenerTodosLosReportesDeUnClubCuandoElClubTieneUnSoloReporteUtilizaElMetodoGuardarYSigueComoClubAccesible(){
        Club club = new Club();
        club.setReportes(new ArrayList<>());
        Reporte reporte = new Reporte();
        club.getReportes().add(reporte);

        when(repositorioReporteMock.obtenerTodosLosReportesDeUnClub(club)).thenReturn(club.getReportes());

        servicioReporte.obtenerTodosLosReportesDeUnClub(club);

        verify(repositorioClubMock, times(1)).guardar(club);
        assertThat(club.getEstaReportado(), equalTo("CLUB ACCESIBLE"));
    }

    @Test
    public void dadoElMetodoObtenerTodosLosReportesDeUnClubCuandoElClubTieneDosReportesUtilizaElMetodoGuardarYSigueComoClubReportado(){
        Club club = new Club();
        club.setReportes(new ArrayList<>());
        Reporte reporte = new Reporte();
        Reporte reporte2 = new Reporte();
        club.getReportes().add(reporte);
        club.getReportes().add(reporte2);

        when(repositorioReporteMock.obtenerTodosLosReportesDeUnClub(club)).thenReturn(club.getReportes());

        servicioReporte.obtenerTodosLosReportesDeUnClub(club);

        verify(repositorioClubMock, times(1)).guardar(club);
        assertThat(club.getEstaReportado(), equalTo("CLUB REPORTADO"));
    }

    @Test
    public void dadoElMetodoagregarNuevoReporteAlClubSiFuncionaAgregaElReporteCorrectamente() throws ReporteExistente, NoExisteEseClub {
        Club club = new Club();
        club.setId(1L);
        club.setReportes(new ArrayList<>());
        Reporte reporte = new Reporte();


        when(repositorioClubMock.buscarClubPor(anyLong())).thenReturn(club);
        when(repositorioReporteMock.buscarReportePorId(anyLong())).thenReturn(null);
        servicioReporte.agregarNuevoReporteAlClub(club.getId(),reporte);

        assertThat(club.getReportes(), contains(reporte));
        verify(repositorioReporteMock,times(1)).guardar(reporte);
    }

    @Test
    public void dadoElMetodoagregarNuevoReporteAlClubCuandoElClubNoExisteLanzaLaExcepcion(){
        Reporte reporte = new Reporte();

        when(repositorioClubMock.buscarClubPor(1L)).thenReturn(null);

        assertThrows(NoExisteEseClub.class, () -> servicioReporte.agregarNuevoReporteAlClub(1L,reporte));
        verify(repositorioReporteMock,times(0)).guardar(reporte);
    }

    @Test
    public void dadoElMetodoagregarNuevoReporteAlClubCuandoElReporteExisteLanzaLaExcepcion(){
        Club club = new Club();
        Reporte reporte = new Reporte();
        reporte.setId(1L);

        when(repositorioClubMock.buscarClubPor(anyLong())).thenReturn(club);
        when(repositorioReporteMock.buscarReportePorId(anyLong())).thenReturn(reporte);

        assertThrows(ReporteExistente.class, () -> servicioReporte.agregarNuevoReporteAlClub(1L,reporte));
        verify(repositorioReporteMock,times(0)).guardar(reporte);
    }
    
}
