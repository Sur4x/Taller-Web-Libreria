package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public class ControladorLike {

    private ServicioLike servicioLike;
    private ServicioPublicacion servicioPublicacion;
    private ServicioClub servicioClub;

    @Autowired
    public ControladorLike(ServicioLike servicioLike, ServicioPublicacion servicioPublicacion, ServicioClub servicioClub){
        this.servicioLike = servicioLike;
        this.servicioPublicacion = servicioPublicacion;
        this.servicioClub = servicioClub;
    }

    @RequestMapping(path = "/club/{clubId}/detallePublicacion/{publicacionId}/likear/{comentarioId}")
    public ModelAndView darLike(@PathVariable Long comentarioId, @PathVariable Long clubId, @PathVariable Long publicacionId, HttpServletRequest request) throws NoExisteEseClub {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Boolean agregado = false;

        if (usuario != null) {
            agregado = servicioLike.agregarLike(comentarioId, usuario);
        }
        if (agregado) {
            model.put("mensaje", "Like agregado correctamente.");
        } else {
            model.put("mensaje", "Problemas al agregar el like.");
        }
        //Agrego los mismos datos al modelo para que la vista detallePublicacion sea la misma.
        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(publicacionId);
        if (publicacion != null) {
            model.put("comentarios", publicacion.getComentarios());
            model.put("comentario", new Comentario());
            model.put("publicacion", publicacion);
            model.put("usuario", usuario);
            model.put("club", servicioClub.buscarClubPor(clubId));
        }
        return new ModelAndView("detallePublicacion", model);
    }
}
