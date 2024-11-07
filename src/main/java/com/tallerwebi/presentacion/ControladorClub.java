package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ControladorClub {

    private ServicioClub servicioClub;
    private ServicioUsuario servicioUsuario;
    private ServicioPublicacion servicioPublicacion;
    private ServicioComentario servicioComentario;
    private ServicioReporte servicioReporte;
    private ServicioLike servicioLike;

    @Autowired
    public ControladorClub(ServicioClub servicioClub, ServicioUsuario servicioUsuario, ServicioPublicacion servicioPublicacion, ServicioComentario servicioComentario, ServicioReporte servicioReporte, ServicioLike servicioLike) {
        this.servicioClub = servicioClub;
        this.servicioUsuario = servicioUsuario;
        this.servicioPublicacion = servicioPublicacion;
        this.servicioComentario = servicioComentario;
        this.servicioReporte = servicioReporte;
        this.servicioLike = servicioLike;
    }

    // Muestra el formulario para crear un nuevo club
    @RequestMapping(path = "/crearClub")
    public ModelAndView irACrearNuevoClub(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        if (usuario != null) {
            modelo.put("club", new Club());
            modelo.put("usuario", usuario);
            return new ModelAndView("crearClub", modelo);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    // Crea un nuevo club
    @RequestMapping(path = "/crearNuevoClub", method = RequestMethod.POST)
    public ModelAndView crearNuevoClub(@ModelAttribute("club") Club club, HttpServletRequest request, @RequestParam("imagen") MultipartFile imagen) throws ClubExistente {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        if (!imagen.isEmpty()) {
            // Usar una ruta absoluta o un directorio dentro de static
            String uploadDir = System.getProperty("user.dir") + "/uploads/";  // Usando la raíz del proyecto
            String imagePath = uploadDir + imagen.getOriginalFilename();
            File dir = new File(uploadDir);

            // Crear la carpeta si no existe
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                imagen.transferTo(new File(imagePath)); // Guardar el archivo
                club.setImagen("/uploads/" + imagen.getOriginalFilename()); // Usar la URL para mostrar la imagen
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen", e);
            }
        }
        Boolean agregado = servicioClub.agregar(club);
        if (agregado && usuario != null) {
            modelo.put("usuario", usuario);
            modelo.put("clubId", club.getId());
            return new ModelAndView("redirect:/club/{clubId}", modelo);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    // Muestra los detalles de un club específico
    @RequestMapping(path = "/club/{clubId}")
    public ModelAndView irADetalleClub(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Club club = servicioClub.buscarClubPor(id);
        ModelMap model = new ModelMap();
        Puntuacion puntuacion = servicioClub.buscarPuntuacion(club, usuario);
        if (club != null) {
            model.addAttribute("puntuacion", puntuacion);
            model.put("club", club);
            model.put("usuario", usuario);
            return new ModelAndView("detalleClub", model);
        } else {
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
        servicioClub.agregarNuevaPublicacion(publicacion, id);
        return new ModelAndView("redirect:/club/{clubId}");
    }


    @RequestMapping(path = "/club/{clubId}/eliminarPublicacion/{publicacionId}")
    @Transactional
    public ModelAndView eliminarPublicacion(@PathVariable("clubId") Long id, @PathVariable("publicacionId") Long idPublicacion, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        Club club = servicioClub.buscarClubPor(id);
        Publicacion publicacion = servicioPublicacion.buscarPublicacionPorId(idPublicacion);

        if (servicioUsuario.esAdmin(usuario)) {
            servicioClub.eliminarPublicacion(publicacion, club);
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
            modelo.put("club", servicioClub.buscarClubPor(clubId));
            return new ModelAndView("detallePublicacion", modelo);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

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

    @RequestMapping(path = "/club/eliminar/{id}", method = RequestMethod.POST)
    public ModelAndView eliminarClub(@PathVariable("id") Long id) throws NoExisteEseClub {
        Club club = servicioClub.buscarClubPor(id);
        if (club != null) {
            servicioClub.eliminarClub(club);
            return new ModelAndView("redirect:/home");
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    @RequestMapping(path = "/club/{clubId}/reportar", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioReporte(@PathVariable("clubId") Long clubId, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap model = new ModelMap();

        Club club = servicioClub.buscarClubPor(clubId);
        if (club != null && usuario != null) {
            model.put("club", club);
            model.put("usuario", usuario);
            model.put("reporte", new Reporte());
            return new ModelAndView("crearReporte", model);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    @RequestMapping(path = "/club/{clubId}/listar/reportes", method = RequestMethod.GET)
    @Transactional
    public ModelAndView mostrarReportesPorClub(@PathVariable("clubId") Long clubId, HttpServletRequest request) throws NoExisteEseClub {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            ModelMap model = new ModelMap();

            if (usuario == null) {
                return new ModelAndView("redirect:/login");
            }
            Club club = servicioClub.buscarClubPor(clubId);

            if (club == null) {
                throw new NoExisteEseClub("No existe el club con ID " + clubId);
            }

            List<Reporte> reportes = servicioReporte.listarReportesPorClub(club);

            servicioClub.obtenerTodosLosReportesDeUnClub(club);

            model.put("club", club);
            model.put("usuario", usuario);
            model.put("reportes", reportes);

            return new ModelAndView("verReportes", model);
        }

    @RequestMapping(path = "/club/{clubId}/nuevoReporte", method = RequestMethod.POST)
    public ModelAndView realizarNuevoReporte(@PathVariable("clubId") Long clubId, @ModelAttribute("reporte") Reporte reporte, HttpServletRequest request) throws NoExisteEseClub, ReporteExistente {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        Club club = servicioClub.buscarClubPor(clubId);
        if (club != null && usuario != null) {
            reporte.setClub(club);
            servicioReporte.guardarReporte(reporte);
            servicioClub.incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(clubId);
            return new ModelAndView("redirect:/club/" + clubId);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    @RequestMapping(path = "/club/eliminar/reporte/{id}", method = RequestMethod.POST)
    public ModelAndView eliminarReporteDeUnClub(@PathVariable("id") Long id) throws NoExisteEseClub {

            servicioReporte.eliminarReporte(id);

            return new ModelAndView("redirect:/home");
    }

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

    @RequestMapping(path = "/ranking")
    public ModelAndView irARanking(HttpServletRequest request) throws NoExisteEseUsuario {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario != null) {
            Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuario.getId());
            model.put("usuario", usuarioActual);
        } else {model.put("usuario", null);}

        List<Club> clubsMiembros = servicioClub.obtenerClubsConMasMiembros();
        List<Club> clubsPuntuacion = servicioClub.obtenerClubsConMejorPuntuacion();
        List<Usuario> usuariosSeguidores = servicioUsuario.obtenerUsuariosConMasSeguidores();
        List<Club> clubsPublicaciones = servicioClub.obtenerClubsConMasPublicaciones();

        model.addAttribute("clubsMiembros", clubsMiembros);
        model.addAttribute("clubsPuntuacion", clubsPuntuacion);
        model.addAttribute("usuariosSeguidores", usuariosSeguidores);
        model.addAttribute("clubsPublicaciones", clubsPublicaciones);

        return new ModelAndView("ranking", model);
    }

    @RequestMapping(path = "/club/puntuar/{id}")
    public String puntuarClub(@PathVariable Long id, @RequestParam Integer puntuacion, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuario.getId());
        Club club = servicioClub.buscarClubPor(id);
        servicioClub.agregarPuntuacion(club, usuarioActual, puntuacion);
        Double puntuacionPromedio = servicioClub.obtenerPuntuacionPromedio(club);
        servicioClub.actualizarPromedio(club, puntuacionPromedio);
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
