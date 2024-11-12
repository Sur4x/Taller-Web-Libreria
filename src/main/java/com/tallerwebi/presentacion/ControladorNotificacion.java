package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorNotificacion {

    @Autowired
    private ServicioNotificacion servicioNotificacion;

    @RequestMapping(path = "/notificacion/listar", method = RequestMethod.GET)
    @Transactional
    public ModelAndView mostrarLasNotificaciones(HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap model = new ModelMap();

        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        List<Notificacion> notificaciones = servicioNotificacion.obtenerElListadoDeNotificaciones();

        model.put("usuario", usuario);
        model.put("notificaciones", notificaciones);

        return new ModelAndView("verNotificaciones", model);
    }
}
