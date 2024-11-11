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
    public void dadoElMetodoagregarNuevoReporteAlClubSiFuncionaAgregaElReporteCorrectamente() throws ReporteExistente, NoExisteEseClub {
        Club club = new Club();
        club.setId(1L);
        club.setReportes(new ArrayList<>());
        Reporte reporte = new Reporte();


        when(repositorioClubMock.buscarClubPor(anyLong())).thenReturn(club);
        when(repositorioReporteMock.buscarReportePorId(anyLong())).thenReturn(null);
        servicioClub.agregarNuevoReporteAlClub(club.getId(),reporte);

        assertThat(club.getReportes(), contains(reporte));
        verify(repositorioReporteMock,times(1)).guardar(reporte);
    }

    @Test
    public void dadoElMetodoagregarNuevoReporteAlClubCuandoElClubNoExisteLanzaLaExcepcion(){
        Reporte reporte = new Reporte();

        when(repositorioClubMock.buscarClubPor(1L)).thenReturn(null);

        assertThrows(NoExisteEseClub.class, () -> servicioClub.agregarNuevoReporteAlClub(1L,reporte));
        verify(repositorioReporteMock,times(0)).guardar(reporte);
    }

    @Test
    public void dadoElMetodoagregarNuevoReporteAlClubCuandoElReporteExisteLanzaLaExcepcion(){
        Club club = new Club();
        Reporte reporte = new Reporte();
        reporte.setId(1L);

        when(repositorioClubMock.buscarClubPor(anyLong())).thenReturn(club);
        when(repositorioReporteMock.buscarReportePorId(anyLong())).thenReturn(reporte);

        assertThrows(ReporteExistente.class, () -> servicioClub.agregarNuevoReporteAlClub(1L,reporte));
        verify(repositorioReporteMock,times(0)).guardar(reporte);
    }

    @Test
    public void dadoUnServicioClubCuandoIntentoAgregarUnaPuntuacionAUnClubSinPuntuacionesPreviasSeSeteaCorrectamente(){
        Club club = new Club();
        Usuario usuario = new Usuario();
        //Puntuacion puntuacion = new Puntuacion();
        when(repositorioClubMock.buscarPuntuacion(club,usuario)).thenReturn(null);

        servicioClub.agregarPuntuacion(club,usuario, 4);

        verify(repositorioClubMock,times(1)).guardarPuntuacion(any());
        assertThat(club.getPuntuaciones().get(0).getPuntuacion(), equalTo(4));
    }
    @Test
    public void dadoUnServicioClubCuandoIntentoAgregarUnaPuntuacionAUnClubConUnaPuntuacionPreviaSeReemplaza(){
        Club club = new Club();
        Usuario usuario = new Usuario();
        Puntuacion puntuacion = new Puntuacion();
        puntuacion.setPuntuacion(5);
        when(repositorioClubMock.buscarPuntuacion(club,usuario)).thenReturn(puntuacion);

        servicioClub.agregarPuntuacion(club,usuario, 4);

        verify(repositorioClubMock,times(1)).guardarPuntuacion(puntuacion);
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

        Double resultado = servicioClub.obtenerPuntuacionPromedio(club);
        assertThat(resultado, equalTo(3.5));
    }


    @Test
    public void dadoElMetodoObtenerPuntuacionPromedioCuandoUnClubTiene0PuntuacionesSuPromedioEsCero(){
        Club club = new Club();
        Double resultado = servicioClub.obtenerPuntuacionPromedio(club);
        assertThat(resultado, equalTo(0.0));
    }

    /*
    @Test
    public void dadoElMetodoActualizarPromedioDebeUtilizarElMetodoDelRepositorioCorrespondiente(){
        Club club = new Club();
        Double promedio = 3.0;

        servicioClub.actualizarPromedio(club,promedio);

        verify(repositorioClubMock,times(1)).actualizarPromedio(club.getId(),promedio);
    }

     */

    @Test
    public void dadoElMetodoActualizarPromedioCuandoCambioSuPromedioSeRealizaCorrectamente(){
        Club club = new Club();
        servicioClub.actualizarPromedio(club,3.0);
        assertThat(club.getPuntuacionPromedio(), equalTo(3.0));
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

}
