package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.ReporteExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioReporteTest {

    private RepositorioReporte repositorioReporteMock;
    private ServicioReporte servicioReporte;

    @BeforeEach
    public void init(){
        repositorioReporteMock = mock(RepositorioReporte.class);
        this.servicioReporte = new ServicioReporteImpl(repositorioReporteMock);
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
        reporte.setId(1L);
        when(repositorioReporteMock.buscarReportePorId(any())).thenReturn(null);

        servicioReporte.guardarReporte(reporte);

        verify(repositorioReporteMock, times(1)).guardar(reporte);
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
        reporte.setId(1L);
        when(repositorioReporteMock.buscarReportePorId(any())).thenReturn(reporte);

        servicioReporte.eliminarReporte(1L);

        verify(repositorioReporteMock,times(1)).eliminar(reporte);
    }

    @Test
    public void dadoElMetodoEliminarReporteSiLeEntregoUnReporteQueNoEstaEnElSistemaDebeNoRealizaElMetodoEliminar(){
        Reporte reporte = new Reporte();
        when(repositorioReporteMock.buscarReportePorId(any())).thenReturn(null);

        servicioReporte.eliminarReporte(1L);

        verify(repositorioReporteMock,times(0)).eliminar(reporte);
    }
    
}
