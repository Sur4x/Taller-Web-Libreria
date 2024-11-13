package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String puntuarClub(@PathVariable Long id, @RequestParam Integer puntuacion, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuario.getId());
        Club club = servicioClub.buscarClubPor(id);
        servicioPuntuacion.agregarPuntuacion(club, usuarioActual, puntuacion);
        Double puntuacionPromedio = servicioPuntuacion.obtenerPuntuacionPromedio(club);
        servicioPuntuacion.actualizarPromedio(club, puntuacionPromedio);
        return "redirect:/club/" + id;
    }

    /* DESPUNTUAR CLUB
    @RequestMapping(path = "/club/despuntuar/{id}")
    public String despuntuarClub(@PathVariable Long id, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuario.getId());
        Club club = servicioClub.buscarClubPor(id);
        servicioClub.removerPuntuacion(club, usuarioActual);
        return "redirect:/club/" + id;
    }
     */
}
