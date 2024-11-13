package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.infraestructura.RepositorioClubImpl;
import com.tallerwebi.infraestructura.RepositorioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioPublicacionTest {

    private RepositorioPublicacion repositorioPublicacionMock;
    private RepositorioClub repositorioClubMock;
    private ServicioPublicacion servicioPublicacion;

    @BeforeEach
    public void init(){
        repositorioPublicacionMock = mock(RepositorioPublicacionImpl.class);
        repositorioClubMock = mock(RepositorioClubImpl.class);
        this.servicioPublicacion = new ServicioPublicacionImpl(repositorioPublicacionMock, repositorioClubMock);
    }

    @Test
    public void dadoElMetodoBuscarPublicacionPorIdCuandoLeEntregoUnIdQueEstaEnElSistemaMeDevuelveLaPublicacion(){
        Publicacion publicacion = new Publicacion();
        publicacion.setComentarios(new ArrayList<>());

        when(repositorioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(publicacion);

        Publicacion publicacionEncontrada = servicioPublicacion.buscarPublicacionPorId(1L);

        assertThat(publicacionEncontrada, equalTo(publicacion));
    }

    @Test
    public void dadoElMetodoBuscarPublicacionPorIdCuandoLeEntregoUnIdQueNoEstaEnElSistemaMeDevuelveLaPublicacionVacia(){
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(repositorioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(null);

        Publicacion publicacionEncontrada = servicioPublicacion.buscarPublicacionPorId(1L);

        assertThat(publicacionEncontrada, equalTo(null));
    }

    @Test
    public void dadoElMetodobuscarPublicacionEnUnClubSiLaEncuentraEnElSistemaLaRetorna(){
        Long publicacionId = 1L;
        Club club = new Club();
        Publicacion publicacion = new Publicacion();
        publicacion.setId(publicacionId);

        when(repositorioPublicacionMock.buscarPublicacionEnUnClub(publicacionId, club)).thenReturn(publicacion);

        Publicacion publicacionObtenida = servicioPublicacion.buscarPublicacionEnUnClub(publicacionId, club);
        assertThat(publicacionObtenida, equalTo(publicacion));
        assertThat(publicacionObtenida.getId(), equalTo(publicacion.getId()));
        verify(repositorioPublicacionMock, times(1)).buscarPublicacionEnUnClub(publicacionId, club);
    }

    @Test
    public void dadoElMetodobuscarPublicacionEnUnClubSiNoLaEncuentraEnElSistemaRetornaNull(){
        Long publicacionId = 1L;
        Club club = new Club();

        when(repositorioPublicacionMock.buscarPublicacionEnUnClub(publicacionId, club)).thenReturn(null);

        Publicacion publicacionObtenida = servicioPublicacion.buscarPublicacionEnUnClub(publicacionId, club);
        assertThat(publicacionObtenida, equalTo(null));
    }

    @Test
    public void dadoElMetodoAgregarNuevaPublicacionCorrectamenteDebeRealizarDosMetodosEspecificos() throws NoExisteEseClub {
        Publicacion publicacion = new Publicacion();
        Club club = new Club();
        club.setId(1L);
        club.setPublicaciones(new ArrayList<>());
        when(repositorioClubMock.buscarClubPor(1L)).thenReturn(club);

        servicioPublicacion.agregarNuevaPublicacion(publicacion, 1L);

        verify(repositorioPublicacionMock, times(1)).guardar(publicacion);
        verify(repositorioClubMock, times(1)).guardar(club);
    }

    @Test
    public void dadoElMetodoAgregarNuevaPublicacionCuandoSuPublicacionOIDSonNulosNoRealizaLosMetodosEspecificos() throws NoExisteEseClub {
        Publicacion publicacion = null;
        Club club = new Club();
        club.setId(1L);
        club.setPublicaciones(new ArrayList<>());
        when(repositorioClubMock.buscarClubPor(1L)).thenReturn(club);

        servicioPublicacion.agregarNuevaPublicacion(publicacion, 1L);

        verify(repositorioPublicacionMock, times(0)).guardar(publicacion);
        verify(repositorioClubMock, times(0)).guardar(club);
    }

    @Test
    public void dadoElMetodoEliminarPublicacionFuncionaCorretamenteRealizaElMetodoGuardarYEliminar(){
        Publicacion publicacion = new Publicacion();
        Club club = new Club();
        club.setPublicaciones(new ArrayList<>());

        servicioPublicacion.eliminarPublicacion(publicacion, club);

        verify(repositorioClubMock, times(1)).guardar(club);
        verify(repositorioPublicacionMock, times(1)).eliminar(publicacion);
    }

    @Test
    public void dadoElMetodoEliminarPublicacionLeProporcionoAlgunObjetoNuloNoRealizaElMetodoGuardarYEliminar(){
        Publicacion publicacion = new Publicacion();
        Club club = null;

        servicioPublicacion.eliminarPublicacion(publicacion, club);

        verify(repositorioClubMock, times(0)).guardar(club);
        verify(repositorioPublicacionMock, times(0)).eliminar(publicacion);
    }

}
