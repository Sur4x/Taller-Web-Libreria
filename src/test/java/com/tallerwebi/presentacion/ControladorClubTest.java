package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ControladorClubTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioClub servicioClubMock;
    private ServicioUsuario servicioUsuarioMock;
    private ControladorClub controladorClub;
    private Usuario usuarioMock;
    private Club clubMock;
    private ServicioPublicacion servicioPublicacionMock;
    private ServicioComentario servicioComentarioMock;
    private ServicioReporte servicioReporteMock;
    private ServicioLike servicioLikeMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class); // Mock del request
        sessionMock = mock(HttpSession.class); // Mock de la sesión
        servicioClubMock = mock(ServicioClub.class); // Mock del servicio de club
        servicioUsuarioMock = mock(ServicioUsuario.class); // Mock del servicio de usuario
        servicioPublicacionMock = mock(ServicioPublicacion.class);
        servicioComentarioMock = mock(ServicioComentario.class);
        servicioReporteMock = mock(ServicioReporte.class);
        servicioLikeMock = mock(ServicioLike.class);
        controladorClub = new ControladorClub(servicioClubMock, servicioUsuarioMock, servicioPublicacionMock, servicioComentarioMock, servicioReporteMock, servicioLikeMock); // Controlador con mocks
        usuarioMock = mock(Usuario.class); // Mock de un usuario
        clubMock = mock(Club.class);


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
    public void dadoElMetodoCrearNuevoClubCuandoSeCreaCorrectamenteMeRedirireccionaAlaVistaDelClubEspecifico() throws ClubExistente, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.agregar(any(Club.class))).thenReturn(true);

        ModelAndView model = controladorClub.crearNuevoClub(new Club(), requestMock, any());
        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/club/{clubId}"));

    }

    @Test
    public void dadoElMetodoCrearNuevoClubCuandoNoSeCreaCorrectamenteMeRedirireccionaAlaVistaHome() throws ClubExistente, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.agregar(any(Club.class))).thenReturn(false);

        ModelAndView model = controladorClub.crearNuevoClub(new Club(), requestMock, any());
        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void dadoElMetodoIrADetalleClubSiElClubExisteDebeRedireccionarmeALaVistaDetalleClub() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Club clubMock = mock(Club.class);
        when(servicioClubMock.buscarClubPor(1L)).thenReturn(clubMock);

        ModelAndView model = controladorClub.irADetalleClub(1L, requestMock);

        assertThat(model.getViewName(), equalToIgnoringCase("detalleClub"));
        assertThat(model.getModel().get("club"), equalTo(clubMock));
        assertThat(model.getModel().get("usuario"), equalTo(usuarioMock));
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
    public void dadoElMetodoIrANuevaPublicacionFuncionaCorretamenteMeDirijeANuevaPublicacion() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        ModelAndView modelo = controladorClub.irANuevaPublicacion(1L, requestMock);
        assertThat(modelo.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(modelo.getViewName(), equalToIgnoringCase("nuevaPublicacion"));
    }

    @Test
    public void dadoElMetodoIrANuevaPublicacionSiElUsuarioNoEstaLogueadoMeDirijeALogin() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);
        ModelAndView modelo = controladorClub.irANuevaPublicacion(1L, requestMock);
        assertThat(modelo.getModel().get("usuario"), equalTo(null));
        assertThat(modelo.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void dadoElMetodoRealizarPublicacionAlAgregarUnaNuevaPublicacionMeDirijeALogin() throws NoExisteEseClub {
        Club club = new Club();
        club.setId(1L);
        when(requestMock.getSession()).thenReturn(sessionMock);
        Publicacion publicacion = new Publicacion();

        ModelAndView modelo = controladorClub.realizarPublicacion(club.getId(),publicacion, requestMock);

        assertThat(modelo.getViewName(), equalToIgnoringCase("redirect:/club/{clubId}"));
    }

    @Test
    public void dadoElMetodoEliminarPublicacionCuandoSoyAdminPuedoEliminarLaPublicacionYMeRedirijeALaVistaEspecificaDelClub() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Club club = new Club();
        club.setId(1L);
        when(servicioClubMock.buscarClubPor(club.getId())).thenReturn(club);
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(servicioPublicacionMock.buscarPublicacionPorId(publicacion.getId())).thenReturn(publicacion);
        when(servicioUsuarioMock.esAdmin(usuarioMock)).thenReturn(true);

        ModelAndView modelo = controladorClub.eliminarPublicacion(club.getId(), publicacion.getId(),requestMock);

        assertThat(modelo.getViewName(), equalToIgnoringCase("redirect:/club/{clubId}"));
    }

    @Test
    public void dadoElMetodoEliminarPublicacionCuandoSoyUsuarioNormalNoPuedoEliminarLaPublicacionYMeRedirijeALaVistaHome() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Club club = new Club();
        club.setId(1L);
        when(servicioClubMock.buscarClubPor(club.getId())).thenReturn(club);
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(servicioPublicacionMock.buscarPublicacionPorId(publicacion.getId())).thenReturn(publicacion);
        when(servicioUsuarioMock.esAdmin(usuarioMock)).thenReturn(false);

        ModelAndView modelo = controladorClub.eliminarPublicacion(club.getId(), publicacion.getId(),requestMock);

        assertThat(modelo.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void dadoElMetodoBuscarClubPorNombreCuandoBuscoAlgoQueTengaResultadoMeRedireccionaAlHomeConUnaListaEnElModelo() throws NoExistenClubs {
        when(requestMock.getSession()).thenReturn(sessionMock);
        ArrayList<Club> clubs = new ArrayList<>();
        clubs.add(new Club());
        when(servicioClubMock.buscarClubPorNombre(any())).thenReturn(clubs);

        ModelAndView modelo = controladorClub.buscarClubPorNombre("palabraPorBuscar",requestMock);

        assertThat(modelo.getViewName(), equalTo("home"));
        assertThat(modelo.getModel().get("clubs"), equalTo(clubs));
        verify(servicioClubMock,times(1)).buscarClubPorNombre("palabraPorBuscar");
    }

    @Test
    public void dadoElMetodoBuscarClubPorNombreCuandoBuscoAlgoQueNOTengaResultadoMeRedireccionaAlHomeConUnaListaVaciaEnElModelo() throws NoExistenClubs {
        when(requestMock.getSession()).thenReturn(sessionMock);
        ArrayList<Club> clubs = new ArrayList<>();
        when(servicioClubMock.buscarClubPorNombre(any())).thenReturn(clubs);

        ModelAndView modelo = controladorClub.buscarClubPorNombre("palabraPorBuscar",requestMock);

        assertThat(modelo.getViewName(), equalTo("home"));
        assertThat(modelo.getModel().get("clubs"), equalTo(null));
        verify(servicioClubMock,times(1)).buscarClubPorNombre("palabraPorBuscar");
    }

    @Test
    public void dadoElMetodoAnotarUsuarioAClubDebeRedireccionarmeALaVistaDelClubEspecifico() throws NoExisteEseClub, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);

        ModelAndView modelo = controladorClub.anotarUsuarioAClub(clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/club/{clubId}"));
        verify(servicioClubMock,times(1)).registrarUsuarioEnElClub(usuarioMock, clubMock);
    }

    @Test
    public void dadoElMetodoAbandonarClubMeDebeRedireccionarALaVistaHome() throws NoExisteEseClub, NoExisteEseUsuario {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);

        ModelAndView modelo = controladorClub.abandonarClub(clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/home"));
        verify(servicioClubMock,times(1)).borrarRegistroUsuarioEnElClub(usuarioMock, clubMock);
    }

    @Test
    public void dadoElMetodoIrDetallePublicacionSiExisteLaPublicacionMeDireccionaALaVistaDeEstaPublicacion() throws NoExisteEseClub {
        Comentario comentario = new Comentario();
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        publicacion.setComentarios(new ArrayList<>());
        publicacion.getComentarios().add(comentario);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(publicacion);

        ModelAndView modelo = controladorClub.irAdetallePublicacion(clubMock.getId(), publicacion.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("detallePublicacion"));
        assertThat(modelo.getModel().get("usuario"), equalTo(usuarioMock));
        assertThat(modelo.getModel().get("club"), equalTo(clubMock));
        assertThat(modelo.getModel().get("publicacion"), equalTo(publicacion));
        assertThat(modelo.getModel().get("comentarios"), equalTo(publicacion.getComentarios()));
    }

    @Test
    public void dadoElMetodoIrDetallePublicacionSiNOExisteLaPublicacionMeDireccionaALaVistaHome() throws NoExisteEseClub {
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(null);

        ModelAndView modelo = controladorClub.irAdetallePublicacion(clubMock.getId(), publicacion.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/home"));
    }

    @Test
    public void dadoElMetodoCrearNuevoComentarioAlmaceneElComentarioCorrectamenteDebeRedireccionarmeALaVistaEspecificaDeLaPublicacion() throws NoExisteEseClub {
        Comentario comentario = new Comentario();
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(servicioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(publicacion);

        ModelAndView modelo = controladorClub.crearNuevoComentario(comentario, publicacion.getId(), clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/club/" + clubMock.getId()  + "/detallePublicacion"+ "/" + publicacion.getId()));
        verify(servicioComentarioMock, times(1)).guardarComentario(comentario, publicacion);
    }

    @Test
    public void dadoElMetodoCrearNuevoComentarioNoAlmaceneElComentarioCorrectamenteDebeRedireccionarmeALaVistaLogin() throws NoExisteEseClub {
        Comentario comentario = new Comentario();
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(servicioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(null);

        ModelAndView modelo = controladorClub.crearNuevoComentario(comentario, publicacion.getId(), clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/login"));
        verify(servicioComentarioMock, times(0)).guardarComentario(comentario, publicacion);
    }

    @Test
    public void dadoElMetodoEliminarClubCuandoLoEliminaExitosamenteMeRedireccionaALaVistaHome() throws NoExisteEseClub {
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);

        ModelAndView modelo = controladorClub.eliminarClub(clubMock.getId());

        assertThat(modelo.getViewName(), equalTo("redirect:/home"));
        verify(servicioClubMock, times(1)).eliminarClub(clubMock);
    }

    @Test
    public void dadoElMetodoEliminarClubCuandoLoNoEliminaExitosamenteMeRedireccionaALaVistaHome() throws NoExisteEseClub {
        when(servicioClubMock.buscarClubPor(any())).thenReturn(null);

        ModelAndView modelo = controladorClub.eliminarClub(clubMock.getId());

        assertThat(modelo.getViewName(), equalTo("redirect:/home"));
        verify(servicioClubMock, times(0)).eliminarClub(clubMock);
    }

    @Test
    public void dadoElMetodoMostrarFormularioReporteCuandoQuieroHacerUnReporteDeUnClubExistenteMeRedireccionaALaVistaCrearReporte() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock);

        ModelAndView modelo = controladorClub.mostrarFormularioReporte(clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("crearReporte"));
        assertThat(modelo.getModel().get("club"), equalTo(clubMock));
        assertThat(modelo.getModel().get("usuario"), equalTo(usuarioMock));
    }

    @Test
    public void dadoElMetodoMostrarFormularioReporteCuandoQuieroHacerUnReporteDeUnClubInexistenteMeRedireccionaALaVistaHome() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(null);

        ModelAndView modelo = controladorClub.mostrarFormularioReporte(clubMock.getId(), requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/login"));
    }

    @Test
    public void dadoElMetodoRealizarNuevoReporteSiExisteElClubAlQueQuieroReportarMeDireccionaALaVistaEspecificaDelClub() throws NoExisteEseClub, ReporteExistente {
        Reporte reporte = new Reporte(); //creo el reporte
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock); //mockeo el usuario
        when(servicioClubMock.buscarClubPor(any())).thenReturn(clubMock); //cuando llamo a buscarClubPor devuelve el club

        ModelAndView modelo = controladorClub.realizarNuevoReporte(clubMock.getId(), reporte, requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/club/" + clubMock.getId()));
        verify(servicioReporteMock, times(1)).guardarReporte(reporte);
    }

    @Test
    public void dadoElMetodoRealizarNuevoReporteSiNOExisteElClubAlQueQuieroReportarMeDireccionaALaVistaHome() throws NoExisteEseClub, ReporteExistente {
        Reporte reporte = new Reporte();
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        when(servicioClubMock.buscarClubPor(any())).thenReturn(null);

        ModelAndView modelo = controladorClub.realizarNuevoReporte(clubMock.getId(), reporte, requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/home"));
        verify(servicioReporteMock, times(0)).guardarReporte(reporte);
    }

    @Test
    public void dadoElMetodoListarReportesSiElUsuarioNoExisteRedireccionaALogin() throws Exception {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorClub.mostrarReportesPorClub(1L, requestMock);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    public void dadoElMetodoMostrarReportesPorClub_ClubNoExiste() throws NoExisteEseClub {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(new Usuario());
        when(servicioClubMock.buscarClubPor(anyLong())).thenReturn(null);

        assertThrows(NoExisteEseClub.class, () -> {
            controladorClub.mostrarReportesPorClub(1L, requestMock);
        });
    }

    @Test
    public void dadoElMetodoMostrarReportesPorClubSeRealizaConExito() throws Exception {
        Usuario usuario = new Usuario();
        Club club = new Club();
        List<Reporte> reportes = new ArrayList<>();

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(servicioClubMock.buscarClubPor(anyLong())).thenReturn(club);
        when(servicioReporteMock.listarReportesPorClub(club)).thenReturn(reportes);

        ModelAndView modelAndView = controladorClub.mostrarReportesPorClub(1L, requestMock);

        assertEquals("verReportes", modelAndView.getViewName());
        assertEquals(club, modelAndView.getModel().get("club"));
        assertEquals(usuario, modelAndView.getModel().get("usuario"));
        assertEquals(reportes, modelAndView.getModel().get("reportes"));
    }

    @Test
    public void dadoElMetodoIrAEliminarComentarioSiLoEliminarCorrectamenteMeRedireccionaALaVistaDetallaClubConUnErrorEspecifico() throws NoExisteEseClub {
        Long clubId = 1L;
        Long publicacionId = 2L;
        Long comentarioId = 3L;
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Comentario comentario = new Comentario();
        comentario.setId(comentarioId);

        Club club = new Club();
        club.setId(clubId);

        Publicacion publicacion = new Publicacion();
        publicacion.setId(publicacionId);

        when(servicioClubMock.buscarClubPor(clubId)).thenReturn(club);
        when(servicioPublicacionMock.buscarPublicacionEnUnClub(publicacionId, club)).thenReturn(publicacion);
        when(servicioComentarioMock.buscarComentarioEnUnaPublicacion(comentarioId, publicacion)).thenReturn(comentario);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);

        ModelAndView modelo = controladorClub.IrAEliminarComentario(clubId, publicacionId,comentarioId, requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/club/" + clubId + "/detallePublicacion/" + publicacionId));
        assertThat(modelo.getModel().get("error"), equalTo("Comentario eliminado correctamente."));
        verify(servicioComentarioMock, times(1)).eliminarComentario(comentario);
        verify(servicioPublicacionMock, times(1)).buscarPublicacionEnUnClub(publicacionId, club);
        verify(servicioComentarioMock, times(1)).buscarComentarioEnUnaPublicacion(comentarioId,publicacion);
    }

    @Test
    public void dadoElMetodoIrAEliminarComentarioSiNoLoPuedoEliminarCorrectamenteMeRedireccionaALaVistaDetallaClubConUnErrorEspecifico() throws NoExisteEseClub {
        Long clubId = 1L;
        Long publicacionId = 2L;
        Long comentarioId = 3L;
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Comentario comentario = new Comentario();
        comentario.setId(comentarioId);

        Club club = new Club();
        club.setId(clubId);

        Publicacion publicacion = new Publicacion();
        publicacion.setId(publicacionId);

        when(servicioClubMock.buscarClubPor(clubId)).thenReturn(club);
        when(servicioPublicacionMock.buscarPublicacionEnUnClub(publicacionId, club)).thenReturn(null);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);

        ModelAndView modelo = controladorClub.IrAEliminarComentario(clubId, publicacionId,comentarioId, requestMock);

        assertThat(modelo.getViewName(), equalTo("redirect:/club/" + clubId + "/detallePublicacion/" + publicacionId));
        assertThat(modelo.getModel().get("error"), equalTo("Error al eliminar el comentario."));
        verify(servicioComentarioMock, times(0)).eliminarComentario(comentario);
        verify(servicioPublicacionMock, times(1)).buscarPublicacionEnUnClub(publicacionId, club);
        verify(servicioComentarioMock, times(0)).buscarComentarioEnUnaPublicacion(comentarioId,publicacion);
    }

    //Estos test son logica del controladorLike, en algun momento moverlos
    //Testear que se use el metodo "agregarLike" una vez
    //Mockear el servicio y quede true o false, con eso testear el mensaje que tiene el modelo
    @Test
    public void dadoUnControladorCuandoDoyLikeCorrectamenteSeAgregaElLikeConUnMensajeExitoso() throws NoExisteEseClub {
        Usuario usuario = new Usuario();
        usuario.setEmail("asd");
        Comentario comentario = new Comentario();
        comentario.setAutor(usuario);
        Club club = new Club();
        club.setNombre("clubsito");
        Publicacion publicacion = new Publicacion();
        publicacion.setMensaje("Asd");

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(servicioLikeMock.agregarLike(comentario.getId(), usuario)).thenReturn(true);
        when(servicioPublicacionMock.buscarPublicacionPorId(publicacion.getId())).thenReturn(publicacion);

        ModelAndView model = controladorClub.darLike(comentario.getId(), club.getId(),publicacion.getId(),requestMock);

        verify(servicioLikeMock,times(1)).agregarLike(comentario.getId(),usuario);//se verifico que el metodo ando auque sea una vez
        verify(servicioPublicacionMock,times(1)).buscarPublicacionPorId(publicacion.getId());
        verify(servicioClubMock,times(1)).buscarClubPor(club.getId());
        assertThat(model.getViewName(),equalTo("detallePublicacion"));
        assertThat(model.getModel().get("mensaje"),equalTo("Like agregado correctamente."));
    }

    @Test
    public void dadoUnControladorLikeCuandoDoyLikeCorrectamenteSeUtilizaElMetodoAgregarLikeYMeLLevaAlaVistaDetallePublicacion() throws NoExisteEseClub {
        Usuario usuario = new Usuario();
        usuario.setEmail("asd");
        Comentario comentario = new Comentario();
        comentario.setAutor(usuario);
        Club club = new Club();
        club.setNombre("clubsito");
        Publicacion publicacion = new Publicacion();
        publicacion.setMensaje("Asd");

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(servicioLikeMock.agregarLike(comentario.getId(), usuario)).thenReturn(false);
        when(servicioPublicacionMock.buscarPublicacionPorId(publicacion.getId())).thenReturn(null);

        ModelAndView model = controladorClub.darLike(comentario.getId(), club.getId(),publicacion.getId(),requestMock);

        verify(servicioLikeMock,times(1)).agregarLike(comentario.getId(),usuario);
        verify(servicioPublicacionMock,times(1)).buscarPublicacionPorId(publicacion.getId());
        verify(servicioClubMock,times(0)).buscarClubPor(club.getId());
        assertThat(model.getViewName(),equalTo("detallePublicacion"));
        assertThat(model.getModel().get("mensaje"),equalTo("Problemas al agregar el like."));
    }




}
