package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExistenClubs;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioClubTest {
    //Voy a mockear el repositorio

    private ServicioClub servicioClub;
    private RepositorioClub repositorioClubMock;
    private  Club clubMock;

    private RepositorioUsuario repositorioUsuarioMock;

    @BeforeEach
    public void init(){
        repositorioClubMock = mock(RepositorioClub.class);
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        this.servicioClub = new ServicioClubImpl(repositorioClubMock);
        clubMock = mock(Club.class);
    }

    @Test
    public void dadoQueUsoElMetodoAgregarYNoTengoElClubAgregadoEnLaBaseDeDatosEsteDevuelveTrueYLoAlmacena() throws ClubExistente {
        when(repositorioClubMock.buscarClubPor(clubMock.getId())).thenReturn(null);

        Boolean agregado = servicioClub.agregar(clubMock);

        assertThat(agregado, is(true));
        verify(repositorioClubMock, times(1)).guardar(clubMock);
    }

    @Test
    public void dadoQueUsoElMetodoAgregarYYaTengoElClubEnLaBaseDeDatosArrojaLaExcepcionClubExistente() throws ClubExistente {
        when(repositorioClubMock.buscarClubPor(clubMock.getId())).thenReturn(clubMock);
        //Se fuerza la excepcion cuando se llama al metodo agregar
        ClubExistente excepcionEsperada = assertThrows(ClubExistente.class, () -> servicioClub.agregar(clubMock));

        verify(repositorioClubMock, times(0)).guardar(clubMock);
        assertEquals(excepcionEsperada.getMessage(), "Ya existe un club con ese nombre");
    }

    @Test
    public void dadoQueUsoElMetodoObtenerTodosLosClubsYTengo2ClubsAgregadosEnLaBaseDeDatosObtengoUnaListaDe2PosicionesDeObjetosClub() throws NoExistenClubs {
        List<Club> clubsDisponibles = new ArrayList<>();
        clubsDisponibles.add(clubMock);
        clubsDisponibles.add(clubMock);
        when(repositorioClubMock.obtenerTodosLosClubs()).thenReturn(clubsDisponibles);

        List<Club> clubs = servicioClub.obtenerTodosLosClubs();

        assertThat(clubs.size(), is(2));
        verify(repositorioClubMock, times(1)).obtenerTodosLosClubs();
    }

    @Test
    public void dadoQueUsoElMetodoObtenerTodosLosClubsYNoTengoNingunClubAgregadoEnLaBaseDeDatosArrojaLaExcepcionNoExistenClubs() throws NoExistenClubs {
        when(repositorioClubMock.obtenerTodosLosClubs()).thenReturn(new ArrayList<>());

        NoExistenClubs excepcionEsperado = assertThrows(NoExistenClubs.class, () -> servicioClub.obtenerTodosLosClubs());

        assertEquals(excepcionEsperado.getMessage(), "No existen clubs registrados");
        verify(repositorioClubMock, times(1)).obtenerTodosLosClubs();
    }

    @Test
    public void dadoQueUsoElMetodoBuscarClubPorYTengoAlmacenadoElClubEnLaBaseDeDatosMeDevuelveElClub() throws NoExisteEseClub {
        when(repositorioClubMock.buscarClubPor(1L)).thenReturn(clubMock);

        Club clubEsperado = servicioClub.buscarClubPor(1L);

        assertEquals(clubEsperado, clubMock);
        verify(repositorioClubMock, times(1)).buscarClubPor(1L);
    }

    @Test
    public void dadoQueUsoElMetodoBuscarClubPorYNoTengoAlmacenadoElClubEnLaBaseDeDatosArrojaLaExcepcionNoExisteEseClub() throws NoExisteEseClub {
        when(repositorioClubMock.buscarClubPor(1L)).thenReturn(null);

        NoExisteEseClub excepcionEsperada = assertThrows(NoExisteEseClub.class, () -> servicioClub.buscarClubPor(1L));

        assertEquals(excepcionEsperada.getMessage(), "No existe un club con ese ID");
        verify(repositorioClubMock, times(1)).buscarClubPor(1L);
    }

    @Test
    public void dadoQueUsoElMetodoBuscarClubPorNombreYTengoAlmacenado2ClubsConEseNombreMeLoDevuelveEnUnaLista(){
        Club clubMock2 = mock(Club.class);
        List<Club> clubsEncontrados = new ArrayList<>();
        clubsEncontrados.add(clubMock);
        clubsEncontrados.add(clubMock2);

        when(repositorioClubMock.buscarClubPorNombre("test")).thenReturn(clubsEncontrados);

        List<Club> clubsEsperados = servicioClub.buscarClubPorNombre("test");

        assertThat(clubsEsperados.size(), is(2));
        verify(repositorioClubMock, times(1)).buscarClubPorNombre("test");
    }

    @Test
    public void dadoQueUsoElMetodoBuscarClubPorNombreYNoTengoAlmacenadoNingunClubsConEseNombreDevuelveUnaListaVacia(){
        when(repositorioClubMock.buscarClubPorNombre("test")).thenReturn(new ArrayList<>());

        List<Club> clubsEsperados = servicioClub.buscarClubPorNombre("test");

        assertThat(clubsEsperados, is(empty()));
        verify(repositorioClubMock, times(1)).buscarClubPorNombre("test");
    }

    @Test
    public void dadoQueVoyAEliminarUnClubPeroNoExisteYLanzaExcepcion() {

        when(repositorioClubMock.buscarClubPor(anyLong())).thenReturn(null);

        Exception exception = assertThrows(NoExisteEseClub.class, () -> servicioClub.eliminarClub(1L));

        assertEquals(exception.getMessage(), "No existe un club con el ID proporcionado.");
    }

    @Test
    public void dadoQueVoyAEliminarUnClubConExisto() throws NoExisteEseClub {

        when(repositorioClubMock.buscarClubPor(anyLong())).thenReturn(clubMock);

        servicioClub.eliminarClub(1L);

        verify(repositorioClubMock,times(1)).eliminar(1L);
    }
}
