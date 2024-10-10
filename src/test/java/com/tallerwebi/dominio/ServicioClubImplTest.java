package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExistenClubs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class ServicioClubImplTest {
    @Mock
    private RepositorioClub repositorioClub;
    @InjectMocks
    private ServicioClubImpl serviceClubImpl;
    private Club club;

    @BeforeEach
    public void init() {
        club = mock(Club.class);
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void debeImpedirAgregarUnClubPorqueYaExiste() throws ClubExistente {

        Mockito.when(repositorioClub.searchClub(any())).thenReturn(true);

        assertThrows(ClubExistente.class, () -> serviceClubImpl.agregar(club));

        verify(repositorioClub, never()).guardar(any(Club.class));

    }

    @Test
    public void debePermitirAgregarUnNuevoClub() throws ClubExistente {

        Mockito.when(repositorioClub.searchClub(any())).thenReturn(false);

        Boolean resultado = serviceClubImpl.agregar(club);

        assertThat(resultado, is(true));

        verify(repositorioClub, times(1)).guardar(club);
    }
    @Test
    public void debeImpedirQueSeObtenganClubesYaQueNoHayNingunoRegistrado(){
        List<Club> listaVaciaDeClubes = new ArrayList<Club>();
        Mockito.when(repositorioClub.obtenerTodosLosClubs()).thenReturn(listaVaciaDeClubes);

        assertThrows(NoExistenClubs.class, ()-> serviceClubImpl.obtenerTodosLosClubs());
    }

    @Test
    public void debePermitirObtenerTodosLosClubs() throws NoExistenClubs {
        List<Club> listaConClubes = new ArrayList<Club>();
        listaConClubes.add(club);

        Mockito.when(repositorioClub.obtenerTodosLosClubs()).thenReturn(listaConClubes);

        List<Club> resultado = serviceClubImpl.obtenerTodosLosClubs();

        assertThat(resultado.size(), is(1));

        verify(repositorioClub, times(1)).obtenerTodosLosClubs();
    }

    @Test
    public void debeImpedirQueSePuedaBuscarUnClubYaQueNoExisteNingunoRegistrado(){

        Mockito.when(repositorioClub.buscarClubPor(anyLong())).thenReturn(null);

        assertThrows(NoExisteEseClub.class, ()-> serviceClubImpl.buscarClubPor(4L));
    }

    @Test
    public void debePermitirQueSePuedaBuscarUnClubPorId() throws NoExisteEseClub {

        Mockito.when(repositorioClub.buscarClubPor(anyLong())).thenReturn(club);

        Club resultado = serviceClubImpl.buscarClubPor(1L);

        assertThat(resultado, is(notNullValue()));

        verify(repositorioClub, times(1)).buscarClubPor(1L);
    }

    @Test
    public void debePermitirQueSePuedaBuscarClubesPorNombre() {

        List<Club> listaConClubes = new ArrayList<Club>();
        listaConClubes.add(club);

        Mockito.when(repositorioClub.buscarClubPorNombre(anyString())).thenReturn(listaConClubes);

        List<Club> resultado = serviceClubImpl.buscarClubPorNombre("Velez");

        assertThat(resultado.size(), is(1));

        verify(repositorioClub, times(1)).buscarClubPorNombre("Velez");
    }

}
