package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPublicacion {

    private ServicioPublicacion servicioPublicacion;
    private ServicioClub servicioClub;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorPublicacion(ServicioPublicacion servicioPublicacion,ServicioClub servicioClub, ServicioUsuario servicioUsuario){
        this.servicioPublicacion = servicioPublicacion;
        this.servicioClub = servicioClub;
        this.servicioUsuario = servicioUsuario;
    }


    //mover a controlador publicacion
    @RequestMapping(path = "/club/{clubId}/irANuevaPublicacion")
    public ModelAndView irANuevaPublicacion(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        if (usuario != null) {
            modelo.put("publicacion", new Publicacion());
            modelo.put("clubId", id);
            modelo.put("usuario", usuario);
            return new ModelAndView("nuevaPublicacion", modelo);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    //arreglar
    @RequestMapping(path = "/club/{clubId}/nuevaPublicacion", method = RequestMethod.POST)
    public ModelAndView realizarPublicacion(@PathVariable("clubId") Long id, @ModelAttribute("publicacion") Publicacion publicacion, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        servicioPublicacion.agregarNuevaPublicacion(publicacion, id);
        return new ModelAndView("redirect:/club/{clubId}");
    }

    //arreglar
    @RequestMapping(path = "/club/{clubId}/eliminarPublicacion/{publicacionId}")
    @Transactional
    public ModelAndView eliminarPublicacion(@PathVariable("clubId") Long id, @PathVariable("publicacionId") Long idPublicacion, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        Club club = this.servicioClub.buscarClubPor(id);
        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(idPublicacion);

        if (this.servicioUsuario.esAdmin(usuario)) {
            servicioPublicacion.eliminarPublicacion(publicacion, club);
            return new ModelAndView("redirect:/club/{clubId}");
        } else return new ModelAndView("redirect:/home");
    }

    @RequestMapping(path = "/club/{clubId}/detallePublicacion/{publicacionId}")
    @Transactional
    public ModelAndView irAdetallePublicacion(@PathVariable("clubId") Long clubId, @PathVariable("publicacionId") Long publicacionId, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(publicacionId);
        if (publicacion != null) {
            modelo.put("comentarios", publicacion.getComentarios());
            modelo.put("comentario", new Comentario());
            modelo.put("publicacion", publicacion);
            modelo.put("usuario", usuario);
            modelo.put("club", this.servicioClub.buscarClubPor(clubId));
            return new ModelAndView("detallePublicacion", modelo);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }
}
