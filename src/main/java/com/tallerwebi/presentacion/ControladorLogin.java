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
    public ControladorLogin(ServicioLogin servicioLogin){
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
            model.put("error", "Usuario o clave incorrecta");
        }
        return new ModelAndView("login", model);
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario) {

        ModelMap model = new ModelMap();
        try{
            servicioLogin.registrar(usuario);
        } catch (UsuarioExistente e){
            model.put("error", "El usuario ya existe");
            return new ModelAndView("registro", model);
        } catch (Exception e){
            model.put("error", "Error al registrar el nuevo usuario");
            return new ModelAndView("registro", model);
        }
        return new ModelAndView("redirect:/login");
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
        try {
            List<Club> clubs = servicioClub.obtenerTodosLosClubs(); //ESTO ES LOGICA DEL CLUB NO DEL LOGIN MOVERLO
            model.put("clubs",clubs);
        }catch (NoExistenClubs e){
            model.put("error", "No existen clubs para mostrar");
        }

        if (usuario != null){
            Usuario usuarioBuscado = servicioUsuario.buscarUsuarioPor(usuario.getId());
            model.put("usuario", usuarioBuscado);
            Set<Usuario> usuariosSeguidos = servicioUsuario.obtenerUsuariosSeguidos(usuarioBuscado);
            List<Publicacion> publicacionesRecientes = servicioPublicacion.obtenerPublicacionesDeUsuariosSeguidos(usuariosSeguidos);
            model.addAttribute("publicacionesRecientes", publicacionesRecientes);
        }else{
            model.put("usuario", null);
        }

        List<Noticia> noticias = servicioNoticia.obtenerNoticiasRandom(3);
        model.addAttribute("noticias", noticias);

        return new ModelAndView("home", model);

    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}





