package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorClub {

    private ServicioClub servicioClub;

    @Autowired
    private ServicioUsuario servicioUsuario;
    @Autowired
    public ControladorClub(ServicioClub servicioClub){
        this.servicioClub = servicioClub;
    }

    //MOVER A CONTROLADOR HOME
    @RequestMapping(path = "/crearClub")
    public ModelAndView irACrearNuevoClub() {
        ModelMap modelo = new ModelMap();
        modelo.put("club", new Club());
        modelo.put("usuario", new Usuario());

        return new ModelAndView("crearClub", modelo);
    }


    @RequestMapping(path = "/crearNuevoClub", method = RequestMethod.POST)
    public ModelAndView crearNuevoClub(@ModelAttribute("club") Club club, HttpServletRequest request) throws ClubExistente, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap model = new ModelMap();

        Boolean agregado = servicioClub.agregar(club);

        if (agregado && usuario != null){
            Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPor(usuario.getId());
            model.put("usuario", usuarioEncontrado);
            return new ModelAndView("redirect:/home");
        }else{
            return new ModelAndView("redirect:/home");
        }

    }

    @RequestMapping(path = "/club/{id}")
    public ModelAndView irADetalleClub(@PathVariable("id") Long id) throws NoExisteEseClub {
        Club club = servicioClub.buscarClubPor(id);
        ModelMap model = new ModelMap();
        if (club != null){
            model.put("club",club);
            model.put("usuario", new Usuario());
            return new ModelAndView("detalleClub", model);
        }else{
            return new ModelAndView("Redirect: /home", model);
        }
    }

}

