package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorClub {

    private ServicioClub servicioClub;

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorClub(ServicioClub servicioClub) {
        this.servicioClub = servicioClub;
    }

    // Muestra el formulario para crear un nuevo club
    @RequestMapping(path = "/crearClub")
    public ModelAndView irACrearNuevoClub() {
        ModelMap modelo = new ModelMap();
        modelo.put("club", new Club());
        modelo.put("usuario", new Usuario());
        return new ModelAndView("crearClub", modelo);
    }

    // Crea un nuevo club
    @RequestMapping(path = "/crearNuevoClub", method = RequestMethod.POST)
    public ModelAndView crearNuevoClub(@ModelAttribute("club") Club club, HttpServletRequest request) throws ClubExistente, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap model = new ModelMap();

        Boolean agregado = servicioClub.agregar(club);

        if (agregado && usuario != null) {
            Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPor(usuario.getId());
            model.put("usuario", usuarioEncontrado);
            return new ModelAndView("redirect:/home");
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    // Muestra los detalles de un club específico
    @RequestMapping(path = "/club/{id}")
    public ModelAndView irADetalleClub(@PathVariable("id") Long id) throws NoExisteEseClub {
        Club club = servicioClub.buscarClubPor(id);
        ModelMap model = new ModelMap();
        if (club != null) {
            model.put("club", club);
            model.put("usuario", new Usuario());
            return new ModelAndView("detalleClub", model);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    // Buscar clubes por nombre o coincidencia parcial
    @RequestMapping(path = "/buscar", method = RequestMethod.GET)
    public ModelAndView buscarClub(@RequestParam("query") String query, HttpServletRequest request) throws NoExistenClubs {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap model = new ModelMap();
        List<Club> clubs = servicioClub.buscarClubPorNombre(query);

        if (clubs.isEmpty()) {
            model.put("usuario", usuario);
            model.put("noResultados", true);
        } else {
            model.put("usuario", usuario);
            model.put("noResultados", false);
            model.put("clubs", clubs);
        }

        return new ModelAndView("home", model);
    }


    @RequestMapping(path = "/club/{clubId}/anotarse", method = RequestMethod.POST)
    public ModelAndView anotarUsuarioAClub(@PathVariable("clubId") Long id, HttpServletRequest request) throws YaFormaParteDelClub, NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Club> clubs = usuario.getClubsInscriptos();
        Club club = servicioClub.buscarClubPor(id);

        if (clubs.contains(club)) {
            throw new YaFormaParteDelClub("El usuario ya forma parte del club");
        }
        usuario.getClubsInscriptos().add(club);
        club.getIntegrantes().add(usuario);
        servicioUsuario.guardarUsuario(usuario);
        return new ModelAndView("redirect:/home");
    }
}
