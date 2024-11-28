package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ControladorEvento {

    private ServicioEvento servicioEvento;
    private ServicioClub servicioClub;
    private ServicioPuntuacion servicioPuntuacion;
    private ServicioMensaje servicioMensaje;

    @Autowired
    public ControladorEvento(ServicioEvento servicioEvento, ServicioClub servicioClub, ServicioPuntuacion servicioPuntuacion, ServicioMensaje servicioMensaje) {
        this.servicioEvento = servicioEvento;
        this.servicioClub = servicioClub;
        this.servicioPuntuacion = servicioPuntuacion;
        this.servicioMensaje = servicioMensaje;
    }

    @RequestMapping(path = "/club/{clubId}/formularioCrearEvento")
    public ModelAndView formularioCrearEvento(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Club club = servicioClub.buscarClubPor(id);
        ModelMap model = new ModelMap();
        if (usuario != null && club != null && (!club.getAdminsSecundarios().contains(usuario) || club.getAdminPrincipal() != usuario)) {
            model.put("evento", new Evento());
            model.put("clubId", id);
            model.put("usuario", usuario);
            return new ModelAndView("formularioCrearEvento", model);
        } else {
            Puntuacion puntuacion = servicioPuntuacion.buscarPuntuacion(club, usuario);
            Evento evento = servicioEvento.obtenerEvento(club); //retorna un evento del club que esté dentro de las últimas 24hs
            model.addAttribute("evento", evento);
            model.addAttribute("puntuacion", puntuacion);
            model.put("club", club);
            model.put("usuario", usuario);
            return new ModelAndView("detalleClub", model); //CORREGIR CON ERROR/MENSAJE
        }
    }

    @RequestMapping(path = "/club/{clubId}/crearEvento", method = RequestMethod.POST)
    public ModelAndView crearEvento(@PathVariable("clubId") Long id, @RequestParam String nombre, @RequestParam String descripcion, @RequestParam(value = "fechaYHora") String fechaYHoraStr, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        Club club = servicioClub.buscarClubPor(id);

        if (nombre != null && descripcion != null && fechaYHoraStr != null) {
            LocalDateTime fechaYHora = LocalDateTime.parse(fechaYHoraStr);
            if(!fechaYHora.isBefore(LocalDateTime.now()) && (club.getAdminPrincipal().equals(usuario) || club.getAdminsSecundarios().contains(usuario))) {
                Evento evento =servicioEvento.setearEvento(nombre, descripcion, fechaYHora, club);
                Boolean existe = servicioEvento.guardar(evento);
                if (existe) {
                    modelo.put("evento", evento);
                    modelo.put("mensaje", "Evento creado correctamente");
                }
            }
            else {
                modelo.put("mensaje", "No se pudo crear el evento");
            }
        } else modelo.put("evento", new Evento());

        return new ModelAndView("redirect:/club/{clubId}", modelo);
    }

    @RequestMapping(path = "/club/{clubId}/evento/{eventoId}")
    public ModelAndView irAEvento(@PathVariable("clubId") Long idClub,@PathVariable("eventoId") Long idEvento, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        Evento evento = servicioEvento.buscarEventoPorId(idEvento);
        if(evento.getEstado().equals("activo")){
            Club club = servicioClub.buscarClubPor(idClub);
            List<Mensaje> mensajesEvento = servicioEvento.obtenerMensajesDeUnEvento(evento);
            modelo.put("evento", evento);
            modelo.put("mensajes", mensajesEvento);
            modelo.put("usuario", usuario);
            modelo.put("club", club);
            return new ModelAndView("evento", modelo);
        }else return new ModelAndView("redirect:/club/{clubId}", modelo);
    }

    @RequestMapping(path = "/club/{clubId}/evento/{eventoId}/crearMensaje", method = RequestMethod.POST)
    public ModelAndView crearMensaje(@PathVariable("clubId") Long idClub,@PathVariable("eventoId") Long idEvento, @RequestParam String texto, HttpServletRequest request){
        ModelMap modelo = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Evento evento = servicioEvento.buscarEventoPorId(idEvento);

        Mensaje mensaje = servicioMensaje.setearMensaje(texto, usuario, evento);
        servicioMensaje.guardar(mensaje);

        return new ModelAndView("redirect:/club/{clubId}/evento/{eventoId}", modelo);
    }

    @RequestMapping(path = "/club/{clubId}/evento/{eventoId}/finalizarEvento")
    public ModelAndView finalizarEvento(@PathVariable("clubId") Long idClub,@PathVariable("eventoId") Long idEvento, HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        Evento evento = servicioEvento.buscarEventoPorId(idEvento);
        servicioEvento.finalizarEvento(evento);
        return new ModelAndView("redirect:/club/{clubId}", modelo);
    }

}
