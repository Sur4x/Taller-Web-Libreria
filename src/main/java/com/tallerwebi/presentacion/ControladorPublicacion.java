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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ControladorPublicacion {

    private ServicioPublicacion servicioPublicacion;
    private ServicioClub servicioClub;
    private ServicioUsuario servicioUsuario;
    private ServicioPuntuacion servicioPuntuacion;

    @Autowired
    public ControladorPublicacion(ServicioPublicacion servicioPublicacion,ServicioClub servicioClub, ServicioUsuario servicioUsuario,ServicioPuntuacion servicioPuntuacion){
        this.servicioPublicacion = servicioPublicacion;
        this.servicioClub = servicioClub;
        this.servicioUsuario = servicioUsuario;
        this.servicioPuntuacion = servicioPuntuacion;
    }

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

    @RequestMapping(path = "/club/{clubId}/nuevaPublicacion", method = RequestMethod.POST)
    public ModelAndView realizarPublicacion(@PathVariable("clubId") Long id, @ModelAttribute("publicacion") Publicacion publicacion, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        servicioPublicacion.agregarNuevaPublicacion(publicacion, id, usuario);
        modelo.put("mensaje", "Nueva publicacion creada");

        Club club = servicioClub.buscarClubPor(id);
        modelo.put("club", club);
        modelo.put("usuario", usuario);
        modelo.put("puntuacion", servicioPuntuacion.buscarPuntuacion(club, usuario));

        return new ModelAndView("detalleClub", modelo);
    }

    @RequestMapping(path = "/club/{clubId}/eliminarPublicacion/{publicacionId}")
    @Transactional
    public ModelAndView eliminarPublicacion(@PathVariable("clubId") Long id, @PathVariable("publicacionId") Long idPublicacion, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Club club = this.servicioClub.buscarClubPor(id);
        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(idPublicacion);

        if (this.servicioUsuario.esAdmin(usuario)) {
            servicioPublicacion.eliminarPublicacion(publicacion, club);
            return new ModelAndView("redirect:/club/{clubId}");
        } else return new ModelAndView("redirect:/home");
    }

    @RequestMapping(path = "/club/{clubId}/detallePublicacion/{publicacionId}")
    public ModelAndView irAdetallePublicacion(@PathVariable("clubId") Long clubId, @PathVariable("publicacionId") Long publicacionId, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(publicacionId);
        if (publicacion != null) {
            //cambia el formato de la fecha
            List<Comentario> comentarios = publicacion.getComentarios();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            for (Comentario comentario : comentarios) {
                LocalDateTime fechaCreacion = comentario.getFechaCreacion();
                if (fechaCreacion != null) {
                    // Formatear la fecha directamente
                    String fechaFormateada = fechaCreacion.format(formatter);
                    comentario.setFechaCreacionFormateada(fechaFormateada);
                }
            }

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
