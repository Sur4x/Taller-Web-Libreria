package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.dom4j.rule.Mode;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorClub {

    private ServicioClub servicioClub;
    private ServicioUsuario servicioUsuario;
    private ServicioPublicacion servicioPublicacion;
    private ServicioComentario servicioComentario;
    private ServicioReporte servicioReporte;

    @Autowired
    public ControladorClub(ServicioClub servicioClub,ServicioUsuario servicioUsuario, ServicioPublicacion servicioPublicacion, ServicioComentario servicioComentario, ServicioReporte servicioReporte) {
        this.servicioClub = servicioClub;
        this.servicioUsuario = servicioUsuario;
        this.servicioPublicacion = servicioPublicacion;
        this.servicioComentario = servicioComentario;
        this.servicioReporte = servicioReporte;
    }

    // Muestra el formulario para crear un nuevo club
    @RequestMapping(path = "/crearClub")
    public ModelAndView irACrearNuevoClub(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        if(usuario != null) {
            modelo.put("club", new Club());
            modelo.put("usuario", usuario);
            return new ModelAndView("crearClub", modelo);
        }else{
            return new ModelAndView("redirect:/login");
        }
    }

    // Crea un nuevo club
    @RequestMapping(path = "/crearNuevoClub", method = RequestMethod.POST)
    public ModelAndView crearNuevoClub(@ModelAttribute("club") Club club, HttpServletRequest request) throws ClubExistente, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Boolean agregado = servicioClub.agregar(club);

        if (agregado && usuario != null){
            modelo.put("usuario", usuario);
            modelo.put("clubId", club.getId());
            return new ModelAndView("redirect:/club/{clubId}", modelo);
        }else{
            return new ModelAndView("redirect:/home");
        }
    }

    // Muestra los detalles de un club específico
    @RequestMapping(path = "/club/{clubId}")
    @Transactional
    public ModelAndView irADetalleClub(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Club club = servicioClub.buscarClubPor(id);
        ModelMap model = new ModelMap();
        if (club != null){
            Hibernate.initialize(club.getPublicaciones());
            model.put("club",club);
            model.put("publicaciones", club.getPublicaciones());
            model.put("usuario", usuario);

            servicioClub.obtenerTodosLosReportesDeUnClub(club);

            return new ModelAndView("detalleClub", model);
        }else{
            return new ModelAndView("Redirect: /home", model);
        }
    }

    // Buscar clubes por nombre o coincidencia parcial
    @RequestMapping(path = "/buscar", method = RequestMethod.GET)
    public ModelAndView buscarClubPorNombre(@RequestParam("query") String query, HttpServletRequest request) throws NoExistenClubs {
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
    public ModelAndView anotarUsuarioAClub(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Club club = servicioClub.buscarClubPor(id);
        servicioClub.registrarUsuarioEnElClub(usuario, club);
        return new ModelAndView("redirect:/club/{clubId}");
    }

    @RequestMapping(path = "/club/{clubId}/abandonar", method = RequestMethod.POST)
    public ModelAndView abandonarClub(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Club club = servicioClub.buscarClubPor(id);
        servicioClub.borrarRegistroUsuarioEnElClub(usuario, club);
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(path = "/club/{clubId}/irANuevaPublicacion")
    @Transactional
    public ModelAndView irANuevaPublicacion(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        if(usuario != null) {
            modelo.put("publicacion", new Publicacion());
            modelo.put("clubId", id);
            modelo.put("usuario", usuario);
            return new ModelAndView("nuevaPublicacion", modelo);
        }else{
            return new ModelAndView("redirect:/login");
        }
    }

    @RequestMapping(path = "/club/{clubId}/nuevaPublicacion", method = RequestMethod.POST)
    public ModelAndView realizarPublicacion(@PathVariable("clubId") Long id, @ModelAttribute("publicacion") Publicacion publicacion, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        servicioClub.agregarNuevaPublicacion(publicacion, id);

        return new ModelAndView("redirect:/club/{clubId}");
    }


    @RequestMapping(path = "/club/{clubId}/eliminarPublicacion/{publicacionId}")
    @Transactional
    public ModelAndView eliminarPublicacion(@PathVariable("clubId") Long id, @PathVariable("publicacionId") Long idPublicacion ,HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        Club club = servicioClub.buscarClubPor(id);
        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(idPublicacion);

        if(servicioUsuario.esAdmin(usuario)){
                servicioClub.eliminarPublicacion(publicacion, club);
                return new ModelAndView("redirect:/club/{clubId}");
        }else return new ModelAndView("redirect:/home");
    }

    @RequestMapping(path = "/club/{clubId}/detallePublicacion/{publicacionId}")
    @Transactional
    public ModelAndView irAdetallePublicacion(@PathVariable("clubId") Long clubId,@PathVariable("publicacionId") Long publicacionId, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(publicacionId);
        if(publicacion != null) {
            Hibernate.initialize(publicacion.getComentarios());
            modelo.put("comentarios", publicacion.getComentarios());
            modelo.put("comentario", new Comentario());
            modelo.put("publicacion", publicacion);
            modelo.put("usuario", usuario);
            modelo.put("club", servicioClub.buscarClubPor(clubId));
            return new ModelAndView("detallePublicacion", modelo);
        }else{
            return new ModelAndView("redirect:/home");
        }
    }
    @RequestMapping(path = "/club/{clubId}/crearNuevoComentario/{publicacionId}")
    @Transactional
    public ModelAndView crearNuevoComentario(@ModelAttribute Comentario comentario,@PathVariable("publicacionId") Long publicacionId, @PathVariable("clubId") Long clubId, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(publicacionId);
        if (publicacion != null){
            comentario.setAutor(usuario);
            comentario.setPublicacion(publicacion);
            servicioComentario.guardarComentario(comentario, publicacion);
        }
        return new ModelAndView("redirect:/club/" + clubId  + "/detallePublicacion"+ "/" + publicacionId);
    }

    @RequestMapping(path = "/club/eliminar/{id}", method = RequestMethod.POST)
    public ModelAndView eliminarClub(@PathVariable("id") Long id) throws NoExisteEseClub {
        Club club = servicioClub.buscarClubPor(id);
        if (club != null) {
            servicioClub.eliminarClub(club);
            return new ModelAndView("redirect:/home");
        }else{
            return new ModelAndView("redirect:/home");
        }
    }
    @RequestMapping(path = "/club/{clubId}/reportar", method = RequestMethod.GET)
    @Transactional
    public ModelAndView mostrarFormularioReporte(@PathVariable("clubId") Long clubId, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap model = new ModelMap();

        Club club = servicioClub.buscarClubPor(clubId);
        if (club != null) {
            Hibernate.initialize(club.getPublicaciones());

            model.put("club", club);
            model.put("usuario", usuario);
            model.put("reporte", new Reporte());

            return new ModelAndView("crearReporte", model);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    @RequestMapping(path = "/club/{clubId}/nuevoReporte", method = RequestMethod.POST)
    public ModelAndView realizarNuevoReporte(@PathVariable("clubId") Long clubId, @ModelAttribute("reporte") Reporte reporte, HttpServletRequest request) throws NoExisteEseClub, ReporteExistente {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        Club club = servicioClub.buscarClubPor(clubId);
        if (club != null) {

            reporte.setClub(club);
            servicioReporte.guardarReporte(reporte);

            return new ModelAndView("redirect:/club/" + clubId);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }
}
