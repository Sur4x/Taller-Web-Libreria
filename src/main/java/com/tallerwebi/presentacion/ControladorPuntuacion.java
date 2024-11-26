package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPuntuacion {

    private ServicioUsuario servicioUsuario;
    private ServicioClub servicioClub;
    private ServicioPuntuacion servicioPuntuacion;

    @Autowired
    public ControladorPuntuacion(ServicioUsuario servicioUsuario, ServicioClub servicioClub, ServicioPuntuacion servicioPuntuacion){
        this.servicioUsuario = servicioUsuario;
        this.servicioClub = servicioClub;
        this.servicioPuntuacion = servicioPuntuacion;
    }

    @RequestMapping(path = "/club/puntuar/{id}")
    public ModelAndView puntuarClub(@PathVariable Long id, @RequestParam Integer puntuacion, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuario.getId());
        Club club = servicioClub.buscarClubPor(id);

        servicioPuntuacion.agregarPuntuacion(club, usuarioActual, puntuacion);

        servicioClub.refrescarClub(club);

        int votos = club.getPuntuaciones().size();
        if (votos >= 3) {
            servicioPuntuacion.obtenerPuntuacionPromedio(club);
            servicioPuntuacion.actualizarPromedio(club);
        }

        model.put("club", club);
        model.put("usuario", usuarioActual);
        model.put("puntuacion", servicioPuntuacion.buscarPuntuacion(club, usuarioActual));
        model.put("mensaje", "Usted puntuo correctamente al club");
        return new ModelAndView("detalleClub", model);
    }

    @RequestMapping(path = "/club/despuntuar/{id}")
    public ModelAndView despuntuarClub(@PathVariable Long id, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuario.getId());
        Club club = servicioClub.buscarClubPor(id);
        servicioPuntuacion.removerPuntuacion(club, usuarioActual);

        model.put("club", club);
        model.put("usuario", usuarioActual);
        model.put("puntuacion", servicioPuntuacion.buscarPuntuacion(club, usuarioActual));
        model.put("mensaje", "Usted elimino su puntuacion correctamente del club");
        return new ModelAndView("detalleClub", model);
    }

}
