package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ServicioPuntuacionTest {

    private ServicioPuntuacion servicioPuntuacion;
    private RepositorioPuntuacion repositorioPuntuacionMock;
    private RepositorioClub repositorioClubMock;

    @BeforeEach
    public void init(){
        repositorioPuntuacionMock = mock(RepositorioPuntuacion.class);
        repositorioClubMock = mock(RepositorioClub.class);
        this.servicioPuntuacion = new ServicioPuntuacionImp(repositorioPuntuacionMock,repositorioClubMock);
    }

    @Test
    public void dadoUnServicioPuntuacionCuandoIntentoAgregarUnaPuntuacionAUnClubSinPuntuacionesPreviasSeSeteaCorrectamente(){
        Club club = new Club();
        Usuario usuario = new Usuario();
        when(repositorioPuntuacionMock.buscarPuntuacion(club,usuario)).thenReturn(null);

        servicioPuntuacion.agregarPuntuacion(club,usuario, 4);

        verify(repositorioPuntuacionMock,times(1)).guardarPuntuacion(any());
        assertThat(club.getPuntuaciones().get(0).getPuntuacion(), equalTo(4));
    }
    @Test
    public void dadoUnServicioPuntuacionCuandoIntentoAgregarUnaPuntuacionAUnClubConUnaPuntuacionPreviaSeReemplaza(){
        Club club = new Club();
        Usuario usuario = new Usuario();
        Puntuacion puntuacion = new Puntuacion();
        puntuacion.setPuntuacion(5);
        when(repositorioPuntuacionMock.buscarPuntuacion(club,usuario)).thenReturn(puntuacion);

        servicioPuntuacion.agregarPuntuacion(club,usuario, 4);

        verify(repositorioPuntuacionMock,times(1)).guardarPuntuacion(puntuacion);
        assertThat(puntuacion.getPuntuacion(), equalTo(4));
    }

    @Test
    public void dadoElMetodoObtenerPuntuacionPromedioCuandoUnClubTiene2PuntuacionesCalculaElPromedioCorrectamente(){
        Puntuacion puntuacion1 = new Puntuacion();
        puntuacion1.setPuntuacion(3);
        Puntuacion puntuacion2 = new Puntuacion();
        puntuacion2.setPuntuacion(4);
        Club club = new Club();
        club.getPuntuaciones().add(puntuacion1);
        club.getPuntuaciones().add(puntuacion2);

        Double resultado = servicioPuntuacion.obtenerPuntuacionPromedio(club);
        assertThat(resultado, equalTo(3.5));
    }

    @Test
    public void dadoElMetodoObtenerPuntuacionPromedioCuandoUnClubTiene0PuntuacionesSuPromedioEsCero(){
        Club club = new Club();
        Double resultado = servicioPuntuacion.obtenerPuntuacionPromedio(club);
        assertThat(resultado, equalTo(0.0));
    }

    @Test
    public void dadoElMetodoActualizarPromedioCuandoUnClubNoTieneNingunaVotacionSuPromedioSigueSiendo0(){
        Club club = new Club();
        servicioPuntuacion.actualizarPromedio(club);
        assertThat(club.getPuntuacionPromedio(), equalTo(0.0));
    }

    @Test
    public void dadoElMetodoRemoverPuntuacionCuandoNoExisteEsaPuntuacionNoHaceNada(){
        Club club = new Club();
        Usuario usuario = new Usuario();
        Puntuacion puntuacion = new Puntuacion();

        when(repositorioPuntuacionMock.buscarPuntuacion(club,usuario)).thenReturn(null);

        servicioPuntuacion.removerPuntuacion(club,usuario);

        verify(repositorioPuntuacionMock,times(1)).buscarPuntuacion(club, usuario);

        verify(repositorioPuntuacionMock,times(0)).eliminarPuntuacion(any(), any());
        verify(repositorioClubMock,times(0)).obtenerPromedioDeUnClub(any());
        verify(repositorioClubMock,times(0)).actualizarPromedioDeUnClub(any(), any());
    }

    @Test
    public void dadoElMetodoRemoverPuntuacionCuandoExisteUnaPuntuacionEliminaLaPuntuacionYActualizaSuPromedio(){
        Club club = new Club();
        Usuario usuario = new Usuario();
        Puntuacion puntuacion = new Puntuacion();
        club.getPuntuaciones().add(puntuacion);

        when(repositorioPuntuacionMock.buscarPuntuacion(club,usuario)).thenReturn(puntuacion);

        servicioPuntuacion.removerPuntuacion(club,usuario);

        verify(repositorioPuntuacionMock,times(1)).buscarPuntuacion(club, usuario);
        verify(repositorioPuntuacionMock,times(1)).eliminarPuntuacion(any(), any());
        verify(repositorioClubMock,times(1)).obtenerPromedioDeUnClub(any());
        verify(repositorioClubMock,times(1)).actualizarPromedioDeUnClub(any(), any());
    }


}
