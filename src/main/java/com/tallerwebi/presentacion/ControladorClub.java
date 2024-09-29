package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.ServicioClub;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorClub {

    private ServicioClub servicioClub;

    @Autowired
    public ControladorClub(ServicioClub servicioClub){
        this.servicioClub = servicioClub;
    }

    @RequestMapping(path = "/crearClub")
    //ARREGLAR ESTE METODO
    public ModelAndView irACrearNuevoClub() {
        Club nuevoClub = new Club(); // Crea una nueva instancia del objeto Club
        ModelAndView modelAndView = new ModelAndView("crearClub");
        modelAndView.addObject("club", nuevoClub); // Agrega el objeto al modelo
        return modelAndView;
    }


    @RequestMapping(path = "/crearNuevoClub", method = RequestMethod.POST)
    public ModelAndView crearNuevoClub(@ModelAttribute("club") Club club) throws ClubExistente {
        Boolean agregado = false;

        agregado = servicioClub.addClub(club);

        if (agregado){
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
            return new ModelAndView("detalleClub", model);
        }else{
            return new ModelAndView("Redirect: /home", model);
        }
    }

}

