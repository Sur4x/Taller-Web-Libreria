package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.hibernate.Hibernate;
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
    private ServicioPuntuacion servicioPuntuacion;

    @Autowired
    public ControladorClub(ServicioClub servicioClub, ServicioUsuario servicioUsuario,ServicioPuntuacion servicioPuntuacion) {
        this.servicioClub = servicioClub;
        this.servicioUsuario = servicioUsuario;
        this.servicioPuntuacion = servicioPuntuacion;
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
        club.setAdminPrincipal(usuario);
        Boolean agregado = servicioClub.agregar(club);
        servicioClub.registrarUsuarioEnElClub(usuario,club);
        if (agregado && usuario != null) {
            modelo.put("usuario", usuario);
            modelo.put("clubId", club.getId());
            return new ModelAndView("redirect:/club/{clubId}", modelo);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    @RequestMapping(path = "/club/{clubId}")
    public ModelAndView irADetalleClub(@PathVariable("clubId") Long id, HttpServletRequest request) throws NoExisteEseClub {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Club club = servicioClub.buscarClubPor(id);
        ModelMap model = new ModelMap();
        Puntuacion puntuacion = servicioPuntuacion.buscarPuntuacion(club, usuario);
        if (club != null) {
            model.addAttribute("puntuacion", puntuacion);
            model.put("club", club);
            model.put("usuario", usuario);
            return new ModelAndView("detalleClub", model);
        } else {
            return new ModelAndView("Redirect: /home", model);
        }
    }

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

    @RequestMapping(path = "/club/{clubId}/echarUsuario/{usuarioId}", method = RequestMethod.POST)
    public ModelAndView echarUsuario(@PathVariable("clubId") Long clubId, @PathVariable("usuarioId") Long usuarioId, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Club club = servicioClub.buscarClubPor(clubId);
        Usuario usuarioPorEchar = servicioUsuario.buscarUsuarioPor(usuarioId);
        Boolean inscripto = servicioClub.usuarioInscriptoEnUnClub(club, usuarioPorEchar);

        if (inscripto){
            servicioClub.echarUsuarioDeUnClub(club, usuarioPorEchar);
        }

        Puntuacion puntuacion = servicioPuntuacion.buscarPuntuacion(club, usuario);

        modelo.put("usuario",usuario);
        modelo.put("club",club);
        modelo.put("puntuacion",puntuacion);

        return new ModelAndView("redirect:/club/" + clubId, modelo);

    }

    @RequestMapping(path = "/club/{clubId}/agregarAdmin/{usuarioId}", method = RequestMethod.POST)
    public ModelAndView hacerAdmin(@PathVariable("clubId") Long clubId, @PathVariable("usuarioId") Long usuarioId, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Club club = servicioClub.buscarClubPor(clubId);
        Usuario nuevoAdmin = servicioUsuario.buscarUsuarioPor(usuarioId);
        Boolean inscripto = servicioClub.usuarioInscriptoEnUnClub(club, nuevoAdmin);

        if (inscripto){
            servicioClub.hacerAdminAUnUsuarioDeUnClub(club, nuevoAdmin);
        }

        Puntuacion puntuacion = servicioPuntuacion.buscarPuntuacion(club, usuario);

        modelo.put("usuario",usuario);
        modelo.put("club",club);
        modelo.put("puntuacion",puntuacion);

        return new ModelAndView("redirect:/club/" + clubId, modelo);
    }

    @RequestMapping(path = "/club/{clubId}/sacarAdmin/{usuarioId}", method = RequestMethod.POST)
    public ModelAndView sacarAdmin(@PathVariable("clubId") Long clubId, @PathVariable("usuarioId") Long usuarioId, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap modelo = new ModelMap();

        Club club = servicioClub.buscarClubPor(clubId);
        Usuario usuarioNormal = servicioUsuario.buscarUsuarioPor(usuarioId);
        Boolean inscripto = servicioClub.usuarioInscriptoEnUnClub(club, usuarioNormal);

        if (inscripto){
            servicioClub.sacarAdminAUnUsuarioDeUnClub(club, usuarioNormal);
        }

        Puntuacion puntuacion = servicioPuntuacion.buscarPuntuacion(club, usuario);

        modelo.put("usuario",usuario);
        modelo.put("club",club);
        modelo.put("puntuacion",puntuacion);

        return new ModelAndView("redirect:/club/" + clubId, modelo);
    }
}
