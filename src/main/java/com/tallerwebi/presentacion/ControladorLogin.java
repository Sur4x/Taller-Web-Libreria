package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenClubs;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;
    @Autowired
    private ServicioClub servicioClub; //Lo puse aca y no en el constructor porque se rompia un test que trae default el proyecto
    @Autowired
    private ServicioUsuario servicioUsuario;
    @Autowired
    private ServicioNoticia servicioNoticia;
    @Autowired
    private ServicioPublicacion servicioPublicacion;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping("/login")
    public ModelAndView irALogin() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
        if (usuarioBuscado != null) {
            //Agregar al model usuarioBuscado y retonarlo en el ModelAndView
            request.getSession().setAttribute("usuario", usuarioBuscado);
            return new ModelAndView("redirect:/home");
        } else {
            model.put("mensaje", "Usuario o clave incorrecta");
        }
        return new ModelAndView("login", model);
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario) {

        ModelMap model = new ModelMap();
        try {
            servicioLogin.registrar(usuario);
        } catch (UsuarioExistente e) {
            model.put("mensaje", "El correo ingresado ya está registrado.");
            return new ModelAndView("registro", model);
        } catch (Exception e) {
            model.put("mensaje", "Error al registrar el nuevo usuario");
            return new ModelAndView("registro", model);
        }
        model.put("datosLogin", new DatosLogin());
        model.put("mensaje", "Registro exitoso");
        return new ModelAndView("login", model);
    }

    @RequestMapping(path = "/registro", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("registro", model);
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView irAHome(HttpServletRequest request) throws NoExistenClubs, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        ModelMap model = new ModelMap();
        List<Club> clubs = servicioClub.obtenerClubsRandom(12);

        List<Noticia> noticias = servicioNoticia.obtenerNoticiasRandom(5);
        model.addAttribute("noticias", noticias);

        if (usuario != null) {
            Usuario usuarioBuscado = servicioUsuario.buscarUsuarioPor(usuario.getId());
            model.addAttribute("usuario", usuarioBuscado);
            Set<Usuario> usuariosSeguidos = servicioUsuario.obtenerUsuariosSeguidos(usuarioBuscado);
            List<Publicacion> publicacionesRecientes = servicioPublicacion.obtenerPublicacionesDeUsuariosSeguidos(usuariosSeguidos);
            model.addAttribute("publicacionesRecientes", publicacionesRecientes);

            if (!usuario.getCategoriasPreferidas().isEmpty() && !usuario.getRol().equals("admin")) {
                List<String> categoriasPreferidas = usuario.getCategoriasPreferidas();
                List<Club> clubsFiltrados = new ArrayList<>();

                for (Club club : clubs) {
                    if (categoriasPreferidas.contains(club.getGenero())) {
                        clubsFiltrados.add(club);
                    }
                }
                model.addAttribute("clubs", clubsFiltrados);
                return new ModelAndView("home", model);
            }
        }

        if (usuario != null && usuario.getRol().equals("admin")){
            List<Club> clubsAdmin = servicioClub.obtenerTodosLosClubs();
            model.addAttribute("clubsAdmin", clubsAdmin);
        }

        model.addAttribute("clubs", clubs);
        return new ModelAndView("home", model);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}






