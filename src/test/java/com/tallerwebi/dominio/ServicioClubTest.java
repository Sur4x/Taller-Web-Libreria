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
    private RepositorioPublicacion repositorioPublicacionMock;
    private RepositorioUsuario repositorioUsuarioMock;
    private RepositorioReporte repositorioReporteMock;
    private  Club clubMock;


    @BeforeEach
    public void init(){
        repositorioClubMock = mock(RepositorioClub.class);
        repositorioPublicacionMock = mock(RepositorioPublicacion.class);
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        repositorioReporteMock = mock(RepositorioReporte.class);

        this.servicioClub = new ServicioClubImpl(repositorioClubMock,repositorioPublicacionMock,repositorioUsuarioMock,repositorioReporteMock);
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
    public void dadoElMetodoRegistrarUsuarioEnElClubNormalYElUsuarioNoEstaInscriptoEnElClubLoRegistraCorrectamente(){
        Usuario usuario = new Usuario();
        Club club = new Club();
        club.setIntegrantes(new ArrayList<>());

        servicioClub.registrarUsuarioEnElClub(usuario, club);

        verify(repositorioUsuarioMock,times(1)).guardar(usuario);
    }

    @Test
    public void dadoElMetodoRegistrarUsuarioEnElClubConUnUsuarioYClubNulosYElUsuarioNoEstaInscriptoEnElClubNoRealizaElMetodoGuardar(){
        Usuario usuario = null;
        Club club = null;

        servicioClub.registrarUsuarioEnElClub(usuario, club);

        verify(repositorioUsuarioMock,times(0)).guardar(usuario);
    }

    @Test
    public void dadoElMetodoborrarRegistroUsuarioEnElClubFuncionaCorrectamenteRealizaElMetodoGuardar(){
        Usuario usuario = new Usuario();
        usuario.setClubsInscriptos(new ArrayList<>());
        Club club = new Club();
        club.setIntegrantes(new ArrayList<>());

        servicioClub.borrarRegistroUsuarioEnElClub(usuario, club);

        verify(repositorioUsuarioMock,times(1)).guardar(usuario);
    }

    @Test
    public void dadoElMetodoborrarRegistroUsuarioEnElClubNoFuncionaCorrectamenteUsuarioOClubNulosNoRealizaElMetodoGuardar(){
        Usuario usuario = null;
        Club club = new Club();
        club.setIntegrantes(new ArrayList<>());

        servicioClub.borrarRegistroUsuarioEnElClub(usuario, club);

        verify(repositorioUsuarioMock,times(0)).guardar(usuario);
    }

    @Test
    public void dadoElMetodoEliminarClubSiLeProporcionoUnClubExistenteEnElSistemaLoEliminaCorrectamente() throws NoExisteEseClub {
        Club club = new Club();
        club.setIntegrantes(new ArrayList<>());
        Usuario usuario = new Usuario();
        club.getIntegrantes().add(usuario); //agregue 1 usuario en la lista del club

        servicioClub.eliminarClub(club);

        verify(repositorioUsuarioMock, times(1)).guardar(usuario);
        verify(repositorioClubMock, times(1)).guardar(club);
        verify(repositorioClubMock, times(1)).eliminar(club);
    }

    @Test
    public void dadoElMetodoEliminarClubSiLeProporcionoUnClubInexistenteEnElSistemaLanzaUnaExcepcion() throws NoExisteEseClub{
        Usuario usuario = new Usuario();
        NoExisteEseClub excepcionEsperada = assertThrows(NoExisteEseClub.class, () -> servicioClub.eliminarClub(null));

        assertEquals(excepcionEsperada.getMessage(), "No existe un club con el ID proporcionado.");
    }

    @Test
    public void dadoElMetodoAgregarNuevaPublicacionCorrectamenteDebeRealizarDosMetodosEspecificos() throws NoExisteEseClub {
        Publicacion publicacion = new Publicacion();
        Club club = new Club();
        club.setId(1L);
        club.setPublicaciones(new ArrayList<>());
        when(repositorioClubMock.buscarClubPor(1L)).thenReturn(club);

        servicioClub.agregarNuevaPublicacion(publicacion, 1L);

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

        servicioClub.agregarNuevaPublicacion(publicacion, 1L);

        verify(repositorioPublicacionMock, times(0)).guardar(publicacion);
        verify(repositorioClubMock, times(0)).guardar(club);
    }

    @Test
    public void dadoElMetodoEliminarPublicacionFuncionaCorretamenteRealizaElMetodoGuardarYEliminar(){
        Publicacion publicacion = new Publicacion();
        Club club = new Club();
        club.setPublicaciones(new ArrayList<>());

        servicioClub.eliminarPublicacion(publicacion, club);

        verify(repositorioClubMock, times(1)).guardar(club);
        verify(repositorioPublicacionMock, times(1)).eliminar(publicacion);
    }

    @Test
    public void dadoElMetodoEliminarPublicacionLeProporcionoAlgunObjetoNuloNoRealizaElMetodoGuardarYEliminar(){
        Publicacion publicacion = new Publicacion();
        Club club = null;

        servicioClub.eliminarPublicacion(publicacion, club);

        verify(repositorioClubMock, times(0)).guardar(club);
        verify(repositorioPublicacionMock, times(0)).eliminar(publicacion);
    }

    @Test
    public void dadoElMetodoObtenerTodosLosReportesDeUnClubCuandoElClubTieneUnSoloReporteUtilizaElMetodoGuardarYSigueComoClubAccesible(){
        Club club = new Club();
        club.setReportes(new ArrayList<>());
        Reporte reporte = new Reporte();
        club.getReportes().add(reporte);

        when(repositorioReporteMock.obtenerTodosLosReportesDeUnClub(club)).thenReturn(club.getReportes());

        servicioClub.obtenerTodosLosReportesDeUnClub(club);

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

        servicioClub.obtenerTodosLosReportesDeUnClub(club);

        verify(repositorioClubMock, times(1)).guardar(club);
        assertThat(club.getEstaReportado(), equalTo("CLUB REPORTADO"));
    }

    @Test
    public void dadoElMetodoAgregarNuevoReporteAlClubCuandoIntentoCrearUnReporteAUnClubInexistenteLanzaUnaExcepcion(){

    }

    @Test
    public void dadoElMetodoAgregarNuevoReporteAlClubCuandoIntentoCrearUnReporteAUnClubExistenteRealizaElMetodoGuardar(){

    }





}
