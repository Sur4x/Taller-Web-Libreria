package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorComentario {

    private ServicioComentario servicioComentario;
    private ServicioPublicacion servicioPublicacion;
    private ServicioClub servicioClub;

    @Autowired
    public ControladorComentario(ServicioComentario servicioComentario,ServicioPublicacion servicioPublicacion, ServicioClub servicioClub){
        this.servicioComentario = servicioComentario;
        this.servicioPublicacion = servicioPublicacion;
        this.servicioClub = servicioClub;
    }

    //esto va en el controlador Comentario
    @RequestMapping(path = "/club/{clubId}/crearNuevoComentario/{publicacionId}")
    public ModelAndView crearNuevoComentario(@ModelAttribute Comentario comentario, @PathVariable("publicacionId") Long publicacionId, @PathVariable("clubId") Long clubId, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(publicacionId);
        if (publicacion != null && usuario != null) {
            servicioComentario.setearAutorYPublicacionEnUnComentario(comentario, usuario, publicacion);
            servicioComentario.guardarComentario(comentario, publicacion);
            return new ModelAndView("redirect:/club/" + clubId + "/detallePublicacion" + "/" + publicacionId);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    //esto va en el controlador Comentario
    @RequestMapping(path = "/club/{clubId}/detallePublicacion/{publicacionId}/eliminarComentario/{comentarioId}")
    public ModelAndView IrAEliminarComentario(@PathVariable("clubId") Long clubId, @PathVariable("publicacionId") Long publicacionId, @PathVariable("comentarioId") Long comentarioId, HttpServletRequest request) throws NoExisteEseClub {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        Club club = servicioClub.buscarClubPor(clubId);
        Publicacion publicacion = servicioPublicacion.buscarPublicacionEnUnClub(publicacionId, club); // publicacion en el club
        Comentario comentario = servicioComentario.buscarComentarioEnUnaPublicacion(comentarioId, publicacion);//coemntario en publicacion

        if (usuario != null && club != null && publicacion != null && comentario != null) {
            servicioComentario.eliminarComentario(comentario);
            model.put("error", "Comentario eliminado correctamente.");
        } else {
            model.put("error", "Error al eliminar el comentario.");
        }
        return new ModelAndView("redirect:/club/" + clubId + "/detallePublicacion/" + publicacionId, model);
    }
}