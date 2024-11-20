package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteNingunaNotificacion;
import com.tallerwebi.infraestructura.RepositorioComentarioImpl;
import com.tallerwebi.infraestructura.RepositorioLikeImpl;
import com.tallerwebi.infraestructura.RepositorioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioNotificacionTest {
    @Mock
    private RepositorioNotificacion repositorioNotificacionMock;
    @InjectMocks
    private ServicioNotificacionImpl servicioNotificacion;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void dadoElMetodoGenerarNotificacionLoHaceCorrectamente(){
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDate.now());
        notificacion.setEvento("Evento test");

        servicioNotificacion.generarNotificacion(notificacion);

        verify(repositorioNotificacionMock, times(1)).crearNotificacion(notificacion);

    }

    @Test
    public void dadoElMetodoBuscarNotificacionPorIdFallaAlNoEncontrarNada() throws NoExisteNingunaNotificacion {

        when(repositorioNotificacionMock.buscarNotificacionPor(anyLong())).thenReturn(null);

        Exception exception = assertThrows(NoExisteNingunaNotificacion.class, () -> {
            servicioNotificacion.buscarNotificacionPorId(1L);
        });

        assertEquals("No hay notificacion registrada con ese id", exception.getMessage());
    }

    @Test
    public void dadoElMetodoBuscarNotificacionPorIdRetornaLaNotificacion() throws NoExisteNingunaNotificacion {
        Long id = 1L;
        Notificacion notificacionEsperada = new Notificacion();
        notificacionEsperada.setId(id);

        when(repositorioNotificacionMock.buscarNotificacionPor(anyLong())).thenReturn(notificacionEsperada);

        Notificacion resultado = servicioNotificacion.buscarNotificacionPorId(id);

        assertNotNull(resultado);
        assertEquals(notificacionEsperada, resultado);

        verify(repositorioNotificacionMock, times(1)).buscarNotificacionPor(id);
    }

    @Test
    public void dadoElMetodoParaObtenerListadosDeNotificacionesDecvuelveListaConNotificaciones() {
        Long id = 1L;
        Notificacion notificacionEsperada = new Notificacion();
        notificacionEsperada.setId(id);

        List<Notificacion> lista = new ArrayList<>();
        lista.add(notificacionEsperada);

        when(repositorioNotificacionMock.listarTodasLasNotificaciones()).thenReturn(lista);

        List<Notificacion> resultado = servicioNotificacion.obtenerElListadoDeNotificaciones();

        assertNotNull(resultado);
        assertEquals(resultado.size(), lista.size());
    }

    @Test
    public void dadoElMetodoParaObtenerListadosDeNotificacionesDecvuelveListaVacia() {

        List<Notificacion> lista = new ArrayList<>();

        when(repositorioNotificacionMock.listarTodasLasNotificaciones()).thenReturn(lista);

        List<Notificacion> resultado = servicioNotificacion.obtenerElListadoDeNotificaciones();

        assertNotNull(resultado);
        assertEquals(resultado.size(), lista.size());
    }
}
