package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
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
    private RepositorioNotificacion repositorioNotificacionMock;
    private  Club clubMock;
    private  Usuario usuarioMock;
    private Notificacion notificacionMock;


    @BeforeEach
    public void init(){
        repositorioClubMock = mock(RepositorioClub.class);
        repositorioPublicacionMock = mock(RepositorioPublicacion.class);
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        repositorioReporteMock = mock(RepositorioReporte.class);
        repositorioNotificacionMock = mock(RepositorioNotificacion.class);

        this.servicioClub = new ServicioClubImpl(repositorioClubMock,repositorioPublicacionMock,repositorioUsuarioMock,repositorioReporteMock,repositorioNotificacionMock);
        clubMock = mock(Club.class);
        usuarioMock = mock(Usuario.class);
        notificacionMock = mock(Notificacion.class);
    }

    @Test
    public void dadoQueUsoElMetodoAgregarYNoTengoElClubAgregadoEnLaBaseDeDatosEsteDevuelveTrueYLoAlmacena() throws ClubExistente {
        when(repositorioClubMock.buscarClubPor(clubMock.getId())).thenReturn(null);;

        Boolean agregado = servicioClub.agregar(clubMock);

        assertThat(agregado, is(true));
        verify(repositorioClubMock, times(1)).guardar(clubMock);

        ArgumentCaptor<Notificacion> captorNotificacion = ArgumentCaptor.forClass(Notificacion.class);
        verify(repositorioNotificacionMock, times(1)).crearNotificacion(captorNotificacion.capture());

        Notificacion notificacionCapturada = captorNotificacion.getValue();
        assertThat(notificacionCapturada.getEvento(), is("Se creo un nuevo club: " + clubMock.getNombre()));
        assertThat(notificacionCapturada.getFecha(), is(LocalDate.now()));
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
        club.getIntegrantes().add(usuario);

        servicioClub.eliminarClub(club);

        verify(repositorioUsuarioMock, times(1)).guardar(usuario);
        verify(repositorioClubMock, times(1)).guardar(club);
        verify(repositorioClubMock, times(1)).eliminar(club);

        ArgumentCaptor<Notificacion> captorNotificacion = ArgumentCaptor.forClass(Notificacion.class);
        verify(repositorioNotificacionMock, times(1)).crearNotificacion(captorNotificacion.capture());

        Notificacion notificacionCapturada = captorNotificacion.getValue();
        assertThat(notificacionCapturada.getEvento(), is("Se elimino un club existente: " + clubMock.getNombre()));
        assertThat(notificacionCapturada.getFecha(), is(LocalDate.now()));
    }

    @Test
    public void dadoElMetodoEliminarClubSiLeProporcionoUnClubInexistenteEnElSistemaLanzaUnaExcepcion() throws NoExisteEseClub{
        Usuario usuario = new Usuario();
        NoExisteEseClub excepcionEsperada = assertThrows(NoExisteEseClub.class, () -> servicioClub.eliminarClub(null));

        assertEquals(excepcionEsperada.getMessage(), "No existe un club con el ID proporcionado.");
    }

    @Test
    public void dadoElMetodoObtenerClubsConMasMiembrosMeDevuelveLaListaDeClubsOrdenada(){
        Club club1 = new Club();
        club1.setCantidadMiembros(3);

        Club club2 = new Club();
        club1.setCantidadMiembros(2);

        List<Club> clubs = new ArrayList<>();
        clubs.add(club1);
        clubs.add(club2);

        when(repositorioClubMock.obtenerClubsConMasMiembros()).thenReturn(clubs);
        List<Club> clubsObtenidos = servicioClub.obtenerClubsConMasMiembros();

        assertThat(clubsObtenidos.size(), equalTo(2));
        verify(repositorioClubMock, times(1)).obtenerClubsConMasMiembros();
    }

    @Test
    public void dadoElMetodoObtenerClubsConMejorPuntuacionMeDevuelveLaListaDeClubsOrdenada(){
        Club club1 = new Club();
        club1.setPuntuacionPromedio(3.5);

        Club club2 = new Club();
        club2.setPuntuacionPromedio(4.7);

        List<Club> clubs = new ArrayList<>();
        clubs.add(club1);
        clubs.add(club2);

        when(repositorioClubMock.obtenerClubsConMejorPuntuacion()).thenReturn(clubs);
        List<Club> clubsObtenidos = servicioClub.obtenerClubsConMejorPuntuacion();

        assertThat(clubsObtenidos.size(), equalTo(2));
        verify(repositorioClubMock, times(1)).obtenerClubsConMejorPuntuacion();
    }

    @Test
    public void dadoElMetodoObtenerClubsConMasPublicacionesMeDevuelveLaListaDeClubsOrdenada(){
        Publicacion publicacion1 = new Publicacion();
        Publicacion publicacion2 = new Publicacion();
        Publicacion publicacion3 = new Publicacion();

        Club club1 = new Club();
        club1.getPublicaciones().add(publicacion1);
        club1.getPublicaciones().add(publicacion2);
        club1.getPublicaciones().add(publicacion3);

        Club club2 = new Club();
        club2.getPublicaciones().add(publicacion1);

        List<Club> clubs = new ArrayList<>();
        clubs.add(club1);
        clubs.add(club2);

        when(repositorioClubMock.obtenerTodosLosClubs()).thenReturn(clubs);

        List<Club> clubsObtenidos = servicioClub.obtenerClubsConMasPublicaciones();

        assertThat(clubsObtenidos.get(0), equalTo(club1));
    }

    @Test
    public void dadoElMetodoEcharUsuarioDeUnClubCuandoElUsuarioEsIntegranteYAdminSecundarioSeEliminaDeAmbos() {
        List<Usuario> adminsSecundarios = new ArrayList<>();
        List<Usuario> integrantes = new ArrayList<>();
        List<Club> clubsAdminSecundarios = new ArrayList<>();
        List<Club> clubsInscriptos = new ArrayList<>();

        adminsSecundarios.add(usuarioMock);
        integrantes.add(usuarioMock);
        clubsAdminSecundarios.add(clubMock);
        clubsInscriptos.add(clubMock);

        when(clubMock.getAdminsSecundarios()).thenReturn(adminsSecundarios);
        when(clubMock.getIntegrantes()).thenReturn(integrantes);
        when(usuarioMock.getClubsAdminSecundarios()).thenReturn(clubsAdminSecundarios);
        when(usuarioMock.getClubsInscriptos()).thenReturn(clubsInscriptos);

        servicioClub.echarUsuarioDeUnClub(clubMock, usuarioMock);

        assertTrue(adminsSecundarios.isEmpty());  // El usuario debe haber sido eliminado de los admins
        assertTrue(integrantes.isEmpty());        // El usuario debe haber sido eliminado de los integrantes
        assertTrue(clubsAdminSecundarios.isEmpty());  // El club debe haber sido eliminado de la lista de clubs del usuario
        assertTrue(clubsInscriptos.isEmpty());    // El club debe haber sido eliminado de la lista de clubs del usuario
    }

    @Test
    public void dadoElMetodoHacerAdminAUnUsuarioDeUnClubCuandoElUsuarioEsIntegranteYNoEsAdminSeActualizanLasListasCorrespondientes() {
        List<Usuario> adminsSecundarios = new ArrayList<>();
        List<Club> clubsAdminSecundarios = new ArrayList<>();

        when(clubMock.getAdminsSecundarios()).thenReturn(adminsSecundarios);
        when(usuarioMock.getClubsAdminSecundarios()).thenReturn(clubsAdminSecundarios);

        servicioClub.hacerAdminAUnUsuarioDeUnClub(clubMock, usuarioMock);

        assertTrue(adminsSecundarios.contains(usuarioMock));
        assertTrue(clubsAdminSecundarios.contains(clubMock));
    }

    @Test
    public void dadoElMetodoSacarAdminAUnUsuarioDeUnClubCuandoElUsuarioEsAdminSeEliminaDeLaListaDeAdmins() {
        List<Usuario> adminsSecundarios = new ArrayList<>();
        List<Club> clubsAdminSecundarios = new ArrayList<>();

        adminsSecundarios.add(usuarioMock);
        clubsAdminSecundarios.add(clubMock);

        when(clubMock.getAdminsSecundarios()).thenReturn(adminsSecundarios);
        when(usuarioMock.getClubsAdminSecundarios()).thenReturn(clubsAdminSecundarios);

        servicioClub.sacarAdminAUnUsuarioDeUnClub(clubMock, usuarioMock);

        assertFalse(adminsSecundarios.contains(usuarioMock));
        assertFalse(clubsAdminSecundarios.contains(clubMock));
    }


}
