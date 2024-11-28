package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class ControladorClub {

    private ServicioClub servicioClub;
    private ServicioUsuario servicioUsuario;
    private ServicioPuntuacion servicioPuntuacion;
    private ServicioNotificacion servicioNotificacion;
    private ServicioNoticia servicioNoticia;
    private ServicioEvento servicioEvento;

    @Autowired
    public ControladorClub(ServicioClub servicioClub, ServicioUsuario servicioUsuario, ServicioPuntuacion servicioPuntuacion, ServicioNotificacion servicioNotificacion, ServicioNoticia servicioNoticia, ServicioEvento servicioEvento) {
        this.servicioClub = servicioClub;
        this.servicioUsuario = servicioUsuario;
        this.servicioPuntuacion = servicioPuntuacion;
        this.servicioNotificacion = servicioNotificacion;
        this.servicioNoticia = servicioNoticia;
        this.servicioEvento = servicioEvento;
    }

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

    @RequestMapping(path = "/crearNuevoClub", method = RequestMethod.POST)
    public ModelAndView crearNuevoClub(@ModelAttribute("club") Club club, HttpServletRequest request, @RequestParam("imagen") MultipartFile imagen) throws ClubExistente, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();
        //Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuario.getId());

        try {
            club.setAdminPrincipal(usuario);
            servicioClub.agregar(club);

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
            }else{
                club.setImagen("/uploads/default.jpg");
            }

            servicioClub.registrarUsuarioEnElClub(usuario, club);

            modelo.put("usuario", usuario);
            modelo.put("clubId", club.getId());
            modelo.put("mensaje", "Club creado exitosamente");
            return new ModelAndView("redirect:/club/{clubId}", modelo);
        } catch (YaExisteUnClubConEseNombre e) {
            modelo.put("mensaje", e.getMessage());
            return new ModelAndView("crearClub", modelo);
        }
    }

    @RequestMapping(path = "/club/{clubId}")
    public ModelAndView irADetalleClub(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Club club = servicioClub.buscarClubPor(id);
        ModelMap model = new ModelMap();
        if (club != null) {
            Puntuacion puntuacion = servicioPuntuacion.buscarPuntuacion(club, usuario);
            Evento evento = servicioEvento.obtenerEvento(club);
            if (evento != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'a las' HH:mm", new Locale("es", "ES"));
                String fechaFormateada = evento.getFechaYHora().format(formatter);
                model.addAttribute("fecha", fechaFormateada);
                model.addAttribute("evento", evento);
            }
            model.addAttribute("puntuacion", puntuacion);
            model.put("club", club);
            model.put("usuario", usuario);
            model.addAttribute("fechaYHoraActual", LocalDateTime.now());
            return new ModelAndView("detalleClub", model);
        } else {
            return new ModelAndView("Redirect: /home", model);
        }
    }

    @RequestMapping(path = "/buscar", method = RequestMethod.GET)
    public ModelAndView buscarClubPorNombre(@RequestParam("query") String query, HttpServletRequest request) throws NoExistenClubs {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap model = new ModelMap();
        model.put("usuario", usuario);
        List<Club> clubs = null;
        try {
            clubs = servicioClub.buscarClubPorNombre(query);
        } catch (NoSeEncontraroClubsConEseNombre e) {
            model.put("clubs", servicioClub.obtenerTodosLosClubs());
            model.put("error", e.getMessage());
            return new ModelAndView("busqueda", model);
        }
        model.put("clubsEncontrados", clubs);
        return new ModelAndView("busqueda", model);
    }

    @RequestMapping(path = "/buscarTodosLosClubs", method = RequestMethod.GET)
    public ModelAndView buscarTodosLosClubs(HttpServletRequest request) throws NoExistenClubs {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap model = new ModelMap();

        List<Club> clubs = servicioClub.obtenerTodosLosClubs();

        model.put("clubsEncontrados", clubs);
        model.put("usuario", usuario);

        return new ModelAndView("busqueda", model);
    }


    @RequestMapping(path = "/club/{clubId}/anotarse", method = RequestMethod.POST)
    public ModelAndView anotarUsuarioAClub(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Club club = servicioClub.buscarClubPor(id);
        servicioClub.registrarUsuarioEnElClub(usuario, club);
        model.put("club", club);
        model.put("usuario", usuario);
        model.put("puntuacion", new Puntuacion());
        model.put("mensaje", "Usted se inscribio corractamente en el club.");
        return new ModelAndView("detalleClub", model);
    }

    @RequestMapping(path = "/club/{clubId}/abandonar", method = RequestMethod.POST)
    public ModelAndView abandonarClub(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Usuario usuarioBuscado = servicioUsuario.buscarUsuarioPor(usuario.getId());
        Club club = servicioClub.buscarClubPor(id);
        servicioClub.borrarRegistroUsuarioEnElClub(usuarioBuscado, club);
        request.getSession().setAttribute("usuario", usuarioBuscado);

        model.put("club", club);
        model.put("usuario", usuarioBuscado);
        model.put("puntuacion", new Puntuacion());
        model.put("mensaje", "Usted abandono corractamente en el club.");
        return new ModelAndView("detalleClub", model);
    }

    @RequestMapping(path = "/club/eliminar/{id}")
    public ModelAndView eliminarClub(@PathVariable("id") Long id, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario, NoExistenClubs {
        ModelMap modelo = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        //Usuario usuarioBuscado = servicioUsuario.buscarUsuarioPor(usuario.getId());
        Club club = servicioClub.buscarClubPor(id);
        if (club != null) {

            for (Usuario integrante : club.getIntegrantes()) {
                Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPor(integrante.getId());
                servicioNotificacion.crearNotificacion(usuarioEncontrado, "clubEliminado", club.getNombre());
            }
            club.setAdminPrincipal(null);
            servicioClub.eliminarClub(club);
            modelo.put("mensaje", "Club eliminado correctamente");
            modelo.put("usuario", usuario);
            List<Noticia> noticias = servicioNoticia.obtenerNoticiasRandom(5);
            modelo.put("noticias", noticias);
            modelo.put("clubs", servicioClub.obtenerTodosLosClubs());
            if(usuario.getRol().equals("admin")){
                modelo.put("clubsAdmin", servicioClub.obtenerTodosLosClubs());
            }
            return new ModelAndView("home", modelo);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    @RequestMapping(path = "/ranking")
    public ModelAndView irARanking(HttpServletRequest request) throws NoExisteEseUsuario {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario != null) {
            Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuario.getId());
            model.put("usuario", usuarioActual);
        } else {
            model.put("usuario", null);
        }

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

    @RequestMapping(path = "/club/{clubId}/echarUsuario/{usuarioId}", method = RequestMethod.POST)
    public ModelAndView echarUsuario(@PathVariable("clubId") Long clubId, @PathVariable("usuarioId") Long usuarioId, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Club club = servicioClub.buscarClubPor(clubId);
        Usuario usuarioPorEchar = servicioUsuario.buscarUsuarioPor(usuarioId);
        Boolean inscripto = servicioClub.usuarioInscriptoEnUnClub(club, usuarioPorEchar);

        if (inscripto) {
            servicioClub.echarUsuarioDeUnClub(club, usuarioPorEchar);
            servicioNotificacion.crearNotificacion(usuarioPorEchar, "usuarioEchado", club.getNombre());
        }

        Puntuacion puntuacion = servicioPuntuacion.buscarPuntuacion(club, usuario);

        modelo.put("usuario", usuario);
        modelo.put("club", club);
        modelo.put("puntuacion", puntuacion);
        modelo.put("mensaje", "Usuario echado correctamente.");
        return new ModelAndView("redirect:/club/" + clubId, modelo);

    }

    @RequestMapping(path = "/club/{clubId}/agregarAdmin/{usuarioId}", method = RequestMethod.POST)
    public ModelAndView hacerAdmin(@PathVariable("clubId") Long clubId, @PathVariable("usuarioId") Long usuarioId, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Club club = servicioClub.buscarClubPor(clubId);
        Usuario nuevoAdmin = servicioUsuario.buscarUsuarioPor(usuarioId);
        Boolean inscripto = servicioClub.usuarioInscriptoEnUnClub(club, nuevoAdmin);

        if (inscripto) {
            servicioClub.hacerAdminAUnUsuarioDeUnClub(club, nuevoAdmin);
        }

        Puntuacion puntuacion = servicioPuntuacion.buscarPuntuacion(club, usuario);

        modelo.put("usuario", usuario);
        modelo.put("club", club);
        modelo.put("puntuacion", puntuacion);
        modelo.put("mensaje", "Admin confirmado");
        return new ModelAndView("redirect:/club/" + clubId, modelo);
    }

    @RequestMapping(path = "/club/{clubId}/sacarAdmin/{usuarioId}", method = RequestMethod.POST)
    public ModelAndView sacarAdmin(@PathVariable("clubId") Long clubId, @PathVariable("usuarioId") Long usuarioId, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Club club = servicioClub.buscarClubPor(clubId);
        Usuario usuarioNormal = servicioUsuario.buscarUsuarioPor(usuarioId);
        Boolean inscripto = servicioClub.usuarioInscriptoEnUnClub(club, usuarioNormal);

        if (inscripto) {
            servicioClub.sacarAdminAUnUsuarioDeUnClub(club, usuarioNormal);
        }

        Puntuacion puntuacion = servicioPuntuacion.buscarPuntuacion(club, usuario);

        modelo.put("usuario", usuario);
        modelo.put("club", club);
        modelo.put("puntuacion", puntuacion);
        modelo.put("mensaje", "admin revocado.");
        return new ModelAndView("redirect:/club/" + clubId, modelo);
    }

    @RequestMapping(path = "/club/{clubId}/editarClub")
    public ModelAndView irAEditarClub(@PathVariable("clubId") Long clubId, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        usuario = servicioUsuario.buscarUsuarioPor(usuario.getId());
        ModelMap modelo = new ModelMap();
        Club club = servicioClub.buscarClubPor(clubId);

        modelo.put("club", club);
        modelo.put("usuario", usuario);

        return new ModelAndView("editarClub", modelo);
    }

    @RequestMapping(path = "/club/{clubId}/editar", method = RequestMethod.POST)
    public ModelAndView editar(@PathVariable("clubId") Long clubId, @ModelAttribute("club") Club club, HttpServletRequest request, @RequestParam("imagen") MultipartFile imagen) throws NoExisteEseClub, YaExisteUnClubConEseNombre, ClubExistente {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
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
        club.setAdminPrincipal(usuario);
        servicioClub.actualizar(club);

        return new ModelAndView("redirect:/club/" + clubId);
    }
}
