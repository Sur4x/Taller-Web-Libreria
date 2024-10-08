package com.tallerwebi.repositorio;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.RepositorioClub;
import net.bytebuddy.matcher.ElementMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class RepositorioClubTest {

    private RepositorioClub repositorioClubMock;

    @BeforeEach
    public void init() {
        repositorioClubMock = mock(RepositorioClub.class); // mock del repo
    }

    @Test
    public void dadoQueElClubExiste_searchClubDebeDevolverTrue() {
        Club club = new Club();
        club.setNombre("Club de Prueba");

        when(repositorioClubMock.searchClub(club)).thenReturn(true);

        Boolean resultado = repositorioClubMock.searchClub(club);

        assertThat(resultado, is(true));
    }

    @Test
    public void dadoQueElClubNoExiste_searchClubDebeDevolverFalse() {
        Club club = new Club();
        club.setNombre("Club de Prueba");
        when(repositorioClubMock.searchClub(club)).thenReturn(false);
        Boolean resultado = repositorioClubMock.searchClub(club);
        assertThat(resultado, is(false));
    }

    @Test
    public void dadoQueExistenLosClubObtenerTodosLosClub(){
        ArrayList<Club> clubs = new ArrayList<Club>();
        clubs.add(new Club());
        clubs.add(new Club());

        when(repositorioClubMock.obtenerTodosLosClubs()).thenReturn(clubs);

        List<Club> resultado = repositorioClubMock.obtenerTodosLosClubs();

        assertThat(resultado.size(), is(2));
        assertThat(resultado,is(clubs));
    }

    @Test
    public void guardarElClub(){
        Club club = new Club();
        club.setNombre("Club de Prueba");
        repositorioClubMock.guardar(club);
        verify(repositorioClubMock).guardar(club);
    }

    @Test
    public void dadoQueExistaClubBuscarPorNombre(){
        List<Club> clubs = new ArrayList<>(); // creo la lista
        clubs.add(new Club()); //agrego clubs
        when(repositorioClubMock.buscarClubPorNombre("club")).thenReturn(clubs);
        List<Club> resultado = repositorioClubMock.buscarClubPorNombre("club");
        assertThat(resultado.isEmpty(), is(false));
        assertThat(resultado,is(clubs));
    }

    @Test
    public void dadoQueNoExistaNingunClubDevolverListaVacia(){
        when(repositorioClubMock.buscarClubPorNombre("club")).thenReturn(new ArrayList<>());

        List<Club> resultado = repositorioClubMock.buscarClubPorNombre("club");
        assertThat(resultado.isEmpty(), is(true));

    }

}
