package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ControladorClubTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Usuario usuarioMock;
    private Club clubMock;
    private ServicioClub servicioClubMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioPuntuacion servicioPuntuacionMock;
    private ServicioNotificacion servicioNotificacionMock;
    private ControladorClub controladorClub;
    private ServicioNoticia servicioNoticiaMock;
    private ServicioEvento servicioEventoMock;

    @BeforeEach
    public void init() {
        usuarioMock = mock(Usuario.class);
        clubMock = mock(Club.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioClubMock = mock(ServicioClub.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioPuntuacionMock = mock(ServicioPuntuacion.class);
        servicioNotificacionMock = mock(ServicioNotificacion.class);
        servicioNoticiaMock = mock(ServicioNoticia.class);
        servicioEventoMock = mock(ServicioEvento.class);
        controladorClub = new ControladorClub(servicioClubMock, servicioUsuarioMock,servicioPuntuacionMock,servicioNotificacionMock,servicioNoticiaMock, servicioEventoMock); // Controlador con mocks
    }

    @Test
    public void dadoElMetodoIrACrearNuevoClubCuandoEstoyLogueadoDebeDevolvermeLaVistaCrearClub() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);

        ModelAndView model = controladorClub.irACrearNuevoClub(requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("crearClub"));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(model.getModel().get("club") instanceof Club, equalTo(true));
    }

    @Test
    public void dadoElMetodoIrACrearNuevoClubCuandoNoEstoyLogueadoDebeDevolvermeLaVistaCrearClub() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView model = controladorClub.irACrearNuevoClub(requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void dadoElMetodoCrearNuevoClubCuandoSeCreaCorrectamenteMeRedirireccionaAlaVistaDelClubEspecifico() throws ClubExistente, NoExisteEseUsuario, YaExisteUnClubConEseNombre {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.agregar(clubMock)).thenReturn(true);
        MultipartFile imagenMock = mock(MultipartFile.class);

        ModelAndView model = controladorClub.crearNuevoClub(new Club(), requestMock, imagenMock);

        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/club/{clubId}"));
    }


    @Test
    public void dadoElMetodoCrearNuevoClubCuandoIntentoCrearUnoConElMismoNombreQueOtroNoPuedo() throws ClubExistente, NoExisteEseUsuario, YaExisteUnClubConEseNombre {
        //Tengo que esperar la excepcion
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.agregar(clubMock)).thenThrow(new YaExisteUnClubConEseNombre());
        MultipartFile imagenMock = mock(MultipartFile.class);

        ModelAndView model = controladorClub.crearNuevoClub(clubMock, requestMock, imagenMock);

        assertThat(model.getModel().get("mensaje"), equalTo("Ya existe un club con ese nombre, intente otro."));
        assertThat(model.getViewName(), equalTo("crearClub"));
    }

    @Test
    public void dadoElMetodoIrADetalleClubSiElClubExisteDebeRedireccionarmeALaVistaDetalleClub() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Club clubMock = mock(Club.class);
        Puntuacion puntuacion = new Puntuacion();
        when(servicioClubMock.buscarClubPor(1L)).thenReturn(clubMock);
        when(servicioPuntuacionMock.buscarPuntuacion(clubMock,usuarioMock)).thenReturn(puntuacion);

        ModelAndView model = controladorClub.irADetalleClub(1L, requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("detalleClub"));
        assertThat(model.getModel().get("club"), equalTo(clubMock));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(model.getModel().get("puntuacion"), equalTo(puntuacion));
    }

    @Test
    public void dadoElMetodoIrADetalleClubSiElClubNOExisteDebeRedireccionarmeALaVistaDetalleClub() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(1L)).thenReturn(null);
        ModelAndView model = controladorClub.irADetalleClub(1L, requestMock);
        assertThat(model.getViewName(), equalToIgnoringCase("Redirect: /home"));
    }

    @Test
    public void dadoElMetodoBuscarClubPorNombreCuandoBuscoAlgoQueTengaResultadoMeRedireccionaAlaVistaBusquedaConUnaListaEnElModelo() throws NoExistenClubs, NoSeEncontraroClubsConEseNombre {
        when(requestMock.getSession()).thenReturn(sessionMock);
        ArrayList<Club> clubs = new ArrayList<>();
        clubs.add(clubMock);
        when(servicioClubMock.buscarClubPorNombre(any())).thenReturn(clubs);

        ModelAndView modelo = controladorClub.buscarClubPorNombre("palabraPorBuscar",requestMock);

        assertThat(modelo.getViewName(), equalTo("busqueda"));
        assertThat(modelo.getModel().get("clubsEncontrados"), equalTo(clubs));
        verify(servicioClubMock,times(1)).buscarClubPorNombre("palabraPorBuscar");
    }

    @Test
    public void dadoElMetodoBuscarClubPorNombreCuandoBuscoAlgoQueNOTengaResultadoMeRedireccionaABusquedaConUnaListaVaciaEnElModelo() throws NoExistenClubs, NoSeEncontraroClubsConEseNombre {
        //ESPERO LA EXCEPCION.
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioClubMock.buscarClubPorNombre("queryInvalido")).thenThrow(new NoSeEncontraroClubsConEseNombre());

        ModelAndView modelo = controladorClub.buscarClubPorNombre("queryInvalido",requestMock);


        assertThat(modelo.getViewName(), equalTo("busqueda"));
        //assertThat(modelo.getModel().get("clubs"), is(empty()));
        verify(servicioClubMock, times(1)).obtenerTodosLosClubs();
        assertEquals("No existe ningun club con ese nombre.", modelo.getModel().get("error"));
    }

    @Test
    public void dadoElMetodoAnotarUsuarioAClubDebeRedireccionarmeALaVistaDelClubEspecifico() throws NoExisteEseClub, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);

        ModelAndView modelo = controladorClub.anotarUsuarioAClub(clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("detalleClub"));
        verify(servicioClubMock,times(1)).registrarUsuarioEnElClub(usuarioMock, clubMock);
    }

    @Test
    public void dadoElMetodoAbandonarClubMeDebeRedireccionarALaVistaHome() throws NoExisteEseClub, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(any())).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);

        ModelAndView modelo = controladorClub.abandonarClub(clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("detalleClub"));
        assertThat(modelo.getModel().get("mensaje"), equalTo("Usted abandono corractamente en el club."));
        verify(servicioClubMock,times(1)).borrarRegistroUsuarioEnElClub(usuarioMock, clubMock);
    }


//    @Test
//    public void dadoElMetodoEliminarClubCuandoLoEliminaExitosamenteMeRedireccionaALaVistaHome() throws NoExisteEseClub, NoExisteEseUsuario, NoExistenClubs {
//        when(requestMock.getSession()).thenReturn(sessionMock);
//        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
//
//        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);
//
//        ModelAndView modelo = controladorClub.eliminarClub(clubMock.getId(), requestMock);
//
//        assertThat(modelo.getViewName(), equalTo("redirect:/home"));
//        verify(servicioClubMock, times(1)).eliminarClub(clubMock);
//    }

    @Test
    public void dadoElMetodoEliminarClubCuandoLoNoEliminaExitosamenteMeRedireccionaALaVistaHome() throws NoExisteEseClub, NoExisteEseUsuario, NoExistenClubs {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);

        when(servicioClubMock.buscarClubPor(any())).thenReturn(null);

        ModelAndView modelo = controladorClub.eliminarClub(clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/home"));
        verify(servicioClubMock, times(0)).eliminarClub(clubMock);
    }

    @Test
    public void dadoElMetodoIrARankingSiNoEstoyLogueadoEnviaUnModeloConUsuarioNuloYMeRedireccionaALaVistaRanking() throws NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView model = controladorClub.irARanking(requestMock);

        assertThat(model.getViewName(), equalTo("ranking"));
        assertThat(model.getModel().get("usuario"), equalTo(null));
    }

    @Test
    public void dadoElMetodoIrARankingSiEstoyLogueadoEnviaUnModeloUsuarioConElDeLaSesionYMeRedireccionaALaVistaRanking() throws NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.buscarUsuarioPor(usuarioMock.getId())).thenReturn(usuarioMock);

        ModelAndView model = controladorClub.irARanking(requestMock);

        assertThat(model.getViewName(), equalTo("ranking"));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
    }

    @Test
    public void dadoElMetodoIrARankingEnElModeloSeAgreganTodosLosRankings() throws NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        when(servicioClubMock.obtenerClubsConMasMiembros()).thenReturn(new ArrayList<>());
        when(servicioClubMock.obtenerClubsConMejorPuntuacion()).thenReturn(new ArrayList<>());
        when(servicioUsuarioMock.obtenerUsuariosConMasSeguidores()).thenReturn(new ArrayList<>());
        when(servicioClubMock.obtenerClubsConMasPublicaciones()).thenReturn(new ArrayList<>());

        ModelAndView model = controladorClub.irARanking(requestMock);

        assertThat(model.getModel().get("clubsMiembros"), notNullValue());
        assertThat(model.getModel().get("clubsPuntuacion"), notNullValue());
        assertThat(model.getModel().get("usuariosSeguidores"), notNullValue());
        assertThat(model.getModel().get("clubsPublicaciones"), notNullValue());
        verify(servicioClubMock,times(1)).obtenerClubsConMasMiembros();
        verify(servicioClubMock,times(1)).obtenerClubsConMasPublicaciones();
    }

    @Test
    public void dadoElMetodoEcharUsuarioSiElUsuarioNoEstaInscriptoNoLoElimina() throws NoExisteEseClub, NoExisteEseUsuario {
        Club club = new Club();
        Usuario usuario = new Usuario();
        Puntuacion puntuacion = new Puntuacion();

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        when(servicioClubMock.buscarClubPor(1L)).thenReturn(club);
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(usuario);
        when(servicioClubMock.usuarioInscriptoEnUnClub(club,usuario)).thenReturn(false);
        when(servicioPuntuacionMock.buscarPuntuacion(club, usuario)).thenReturn(puntuacion);

        ModelAndView model = controladorClub.echarUsuario(1L,1L, requestMock);

        verify(servicioClubMock,times(0)).echarUsuarioDeUnClub(club,usuario);
        assertThat(model.getViewName(), equalTo("redirect:/club/1"));
    }

    @Test
    public void dadoElMetodoEcharUsuarioSiElUsuarioEstaInscriptoLoEliminaCorrectamente() throws NoExisteEseClub, NoExisteEseUsuario {
        Club club = new Club();
        Usuario usuario = new Usuario();
        Puntuacion puntuacion = new Puntuacion();

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        when(servicioClubMock.buscarClubPor(1L)).thenReturn(club);
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(usuario);
        when(servicioClubMock.usuarioInscriptoEnUnClub(club,usuario)).thenReturn(true);
        when(servicioPuntuacionMock.buscarPuntuacion(club, usuario)).thenReturn(puntuacion);

        ModelAndView model = controladorClub.echarUsuario(1L,1L, requestMock);

        verify(servicioClubMock,times(1)).echarUsuarioDeUnClub(club,usuario);
        assertThat(model.getViewName(), equalTo("redirect:/club/1"));
    }

    @Test
    public void dadoElMetodoHacerAdminSiElUsuarioEstaInscriptoLoHaceAdminCorrectamente() throws NoExisteEseClub, NoExisteEseUsuario {
        Club club = new Club();
        Usuario usuario = new Usuario();
        Puntuacion puntuacion = new Puntuacion();

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        when(servicioClubMock.buscarClubPor(1L)).thenReturn(club);
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(usuario);
        when(servicioClubMock.usuarioInscriptoEnUnClub(club,usuario)).thenReturn(true);
        when(servicioPuntuacionMock.buscarPuntuacion(club, usuario)).thenReturn(puntuacion);

        ModelAndView model = controladorClub.hacerAdmin(1L,1L, requestMock);

        verify(servicioClubMock,times(1)).hacerAdminAUnUsuarioDeUnClub(club,usuario);
        assertThat(model.getViewName(), equalTo("redirect:/club/1"));
    }

    @Test
    public void dadoElMetodoSacarAdminSiElUsuarioEstaInscriptoLeSacaElAdminCorrectamente() throws NoExisteEseClub, NoExisteEseUsuario {
        Club club = new Club();
        Usuario usuario = new Usuario();
        Puntuacion puntuacion = new Puntuacion();

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        when(servicioClubMock.buscarClubPor(1L)).thenReturn(club);
        when(servicioUsuarioMock.buscarUsuarioPor(1L)).thenReturn(usuario);
        when(servicioClubMock.usuarioInscriptoEnUnClub(club,usuario)).thenReturn(true);
        when(servicioPuntuacionMock.buscarPuntuacion(club, usuario)).thenReturn(puntuacion);

        ModelAndView model = controladorClub.sacarAdmin(1L,1L, requestMock);

        verify(servicioClubMock,times(1)).sacarAdminAUnUsuarioDeUnClub(club,usuario);
        assertThat(model.getViewName(), equalTo("redirect:/club/1"));
    }



}
