package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Notificacion;
import com.tallerwebi.dominio.ServicioNotificacion;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorNotificacionTest {

    @Mock
    private ServicioNotificacion servicioNotificacionMock;

    @InjectMocks
    private ControladorNotificacion controladorNotificacion;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
    }

    @Test
    void mostrarLasNotificaciones_usuarioNoAutenticado_redirigeALogin() throws NoExisteEseClub {

        request.getSession().removeAttribute("usuario");

        ModelAndView modelAndView = controladorNotificacion.mostrarLasNotificaciones(request);

        assertEquals("redirect:/login", modelAndView.getViewName());
        verifyNoInteractions(servicioNotificacionMock);
    }


    /*
    @Test
    void mostrarLasNotificaciones_usuarioAutenticado_muestraNotificaciones() throws NoExisteEseClub {

        Usuario usuarioMock = new Usuario();
        usuarioMock.setNombreUsuario("Juan");

        List<Notificacion> notificacionesMock = new ArrayList<>();
        Notificacion notificacion1 = new Notificacion();
        //notificacion1.setEvento("Evento 1");
        notificacionesMock.add(notificacion1);

        request.getSession().setAttribute("usuario", usuarioMock);

        when(servicioNotificacionMock.obtenerElListadoDeNotificaciones()).thenReturn(notificacionesMock);

        ModelAndView modelAndView = controladorNotificacion.mostrarLasNotificaciones(request);

        assertEquals("verNotificaciones", modelAndView.getViewName());
        assertEquals(usuarioMock, modelAndView.getModel().get("usuario"));
        assertEquals(notificacionesMock, modelAndView.getModel().get("notificaciones"));
        verify(servicioNotificacionMock, times(1)).obtenerElListadoDeNotificaciones();
    }*/
}
