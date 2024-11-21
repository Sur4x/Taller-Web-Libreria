
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioNotificacionTest {

    private RepositorioNotificacion repositorioNotificacionMock;
    private RepositorioUsuario repositorioUsuarioMock;
    private ServicioNotificacionImpl servicioNotificacion;

    @BeforeEach
    public void setUp() {
       this.repositorioNotificacionMock = mock(RepositorioNotificacion.class);
        this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
       this.servicioNotificacion = new ServicioNotificacionImpl(repositorioNotificacionMock, repositorioUsuarioMock);
    }

    @Test
    public void dadoElMetodoGenerarNotificacionLoHaceCorrectamente(){
        Notificacion notificacion = new Notificacion();

        servicioNotificacion.generarNotificacion(notificacion);

        verify(repositorioNotificacionMock, times(1)).guardar(notificacion);
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
    public void dadoElMetodoObtenerElListadoDeNotificacionesDeUnUsuarioDecvuelveListaConNotificaciones() {
        Usuario usuario = new Usuario();
        Long id = 1L;
        Notificacion notificacionEsperada = new Notificacion();
        notificacionEsperada.setId(id);

        List<Notificacion> lista = new ArrayList<>();
        lista.add(notificacionEsperada);

        when(repositorioNotificacionMock.listarTodasLasNotificacionesDeUnUsuario(any())).thenReturn(lista);

        List<Notificacion> resultado = servicioNotificacion.obtenerElListadoDeNotificacionesDeUnUsuario(usuario);

        assertNotNull(resultado);
        assertEquals(resultado.size(), lista.size());
    }

    @Test
    public void dadoElMetodoParaObtenerListadosDeNotificacionesDecvuelveListaVacia() {
        Usuario usuario = new Usuario();
        List<Notificacion> lista = new ArrayList<>();

        when(repositorioNotificacionMock.listarTodasLasNotificacionesDeUnUsuario(any())).thenReturn(lista);

        List<Notificacion> resultado = servicioNotificacion.obtenerElListadoDeNotificacionesDeUnUsuario(usuario);

        assertNotNull(resultado);
        assertEquals(resultado.size(), lista.size());
    }

    //Testear los 2 metodos crearNotificacion:
    @Test
    public void dadoElMetodoCrearNotificacionCon3ParametrosUtilizaElMetodoCrearNotificacionCon4ParametrosYSeteaMensajeReporteEnNull(){
        Usuario usuario = new Usuario();
        String tipoNotificacion = "nuevoReporte";
        String nombreClub = "pepitos";

        servicioNotificacion.crearNotificacion(usuario, tipoNotificacion, nombreClub);

        verify(repositorioNotificacionMock,times(1)).guardar(any());
        verify(repositorioUsuarioMock,times(1)).guardar(any());
    }


    @Test
    public void dadoElMetodoCrearNotificacionCon4ParametrosCreaUnaNoticiaCorrectamente(){
        Usuario usuario = new Usuario();
        String tipoNotificacion = "nuevoReporte";
        String nombreClub = "pepitos";
        String mensajeReporte = "Reportado por insultar";

        servicioNotificacion.crearNotificacion(usuario, tipoNotificacion, nombreClub, mensajeReporte);

        verify(repositorioNotificacionMock,times(1)).guardar(any());
        verify(repositorioUsuarioMock,times(1)).guardar(any());
    }


}
