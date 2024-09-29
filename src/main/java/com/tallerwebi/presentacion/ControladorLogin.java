package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenClubs;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;
    @Autowired
    private ServicioClub servicioClub; //Lo puse aca y no en el constructor porque se rompia un test que trae default el proyecto
    @Autowired
    private ServicioUsuario servicioUsuario;
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
            request.getSession().setAttribute("rol", usuarioBuscado.getRol());
            request.getSession().setAttribute("id", usuarioBuscado.getId()); //aca seteo el id de la sesion
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
            return new ModelAndView("nuevo-usuario", model);
        } catch (Exception e){
            model.put("error", "Error al registrar el nuevo usuario");
            return new ModelAndView("nuevo-usuario", model);
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
    public ModelAndView irAHome(HttpSession sesion) throws NoExistenClubs, NoExisteEseUsuario {
        Long id = (Long) sesion.getAttribute("id"); //apenas el usuario entra al home obtengo su id
        //String rol = (String) session.getAttribute("rol");
        ModelMap model = new ModelMap();

        List<Club> clubs = servicioClub.obtenerTodosLosClubs(); //ESTO ES LOGICA DEL CLUB NO DEL LOGIN MOVERLO
        model.put("clubs",clubs);

        if (id != null){
            Usuario usuario = servicioUsuario.buscarUsuarioPor(id);
            model.put("usuario", usuario);
        }else{
            model.put("usuario", null);
        }
        return new ModelAndView("home", model);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}





