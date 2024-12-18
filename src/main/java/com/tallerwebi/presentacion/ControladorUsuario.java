package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorUsuario {

    private ServicioNotificacion servicioNotificacion;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario,ServicioNotificacion servicioNotificacion){
        this.servicioUsuario = servicioUsuario;
        this.servicioNotificacion = servicioNotificacion;
    }

    @RequestMapping(path = "/perfil/{id}", method = RequestMethod.GET)
    public ModelAndView irAPerfil(@PathVariable("id") Long id, HttpServletRequest request) throws NoExisteEseUsuario {
        ModelMap model = new ModelMap();

        Usuario usuarioPerfil = servicioUsuario.buscarUsuarioPor(id);

        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuario");

        if (usuarioSesion != null) {
            usuarioSesion = servicioUsuario.buscarUsuarioPor(usuarioSesion.getId());

            boolean sigueAlUsuario = servicioUsuario.obtenerUsuariosSeguidos(usuarioSesion).contains(usuarioPerfil);

            model.addAttribute("usuario", usuarioSesion);
            model.addAttribute("usuarioPerfil", usuarioPerfil);
            model.addAttribute("sigueAlUsuario", sigueAlUsuario);
            model.addAttribute("usuarioActual", usuarioSesion);
            model.addAttribute("cantidadSeguidores", usuarioPerfil.getSeguidores().size());
            model.addAttribute("cantidadSeguidos", usuarioPerfil.getSeguidos().size());

            model.addAttribute("seguidores", usuarioPerfil.getSeguidores());
            model.addAttribute("seguidos", usuarioPerfil.getSeguidos());
        } else {
            model.put("usuario", null);
            model.put("usuarioPerfil", usuarioPerfil);
            model.addAttribute("cantidadSeguidores", usuarioPerfil.getSeguidores().size());
            model.addAttribute("cantidadSeguidos", usuarioPerfil.getSeguidos().size());
            model.addAttribute("seguidores", usuarioPerfil.getSeguidores());
            model.addAttribute("seguidos", usuarioPerfil.getSeguidos());
        }
        return new ModelAndView("perfil", model);
    }

    @RequestMapping(path = "perfil/{id}/seguir")
    public String seguirUsuario(@PathVariable("id") Long idUsuarioASeguir, HttpServletRequest request) throws NoExisteEseUsuario {
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuario");
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuarioSesion.getId());
        Usuario usuarioASeguir = servicioUsuario.buscarUsuarioPor(idUsuarioASeguir);

        if(usuarioASeguir!=null && usuarioActual!=null && !usuarioASeguir.equals(usuarioActual)){
            servicioUsuario.seguirUsuario(usuarioASeguir, usuarioActual);
            servicioNotificacion.crearNotificacion(usuarioASeguir, "nuevoSeguidor", null);
            return "redirect:/perfil/" + idUsuarioASeguir;
        }else{
            return "redirect:/home";
        }
    }

    @RequestMapping(path = "perfil/{id}/dejarDeSeguir")
    public String dejarDeSeguirUsuario(@PathVariable("id") Long idUsuarioASeguir, HttpServletRequest request) throws NoExisteEseUsuario {
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuario");
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuarioSesion.getId());
        Usuario usuarioASeguir = servicioUsuario.buscarUsuarioPor(idUsuarioASeguir);

        if(usuarioASeguir!=null && usuarioActual!=null && !usuarioASeguir.equals(usuarioActual)){
            servicioUsuario.dejarDeSeguirUsuario(usuarioASeguir, usuarioActual);
            return "redirect:/perfil/" + idUsuarioASeguir;
        }else{
            return "redirect:/home";
        }

    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(path = "/misClubs/{usuarioId}")
    public ModelAndView irAMisClubs(@PathVariable("usuarioId") Long id) throws NoExisteEseUsuario {
        Usuario usuario = servicioUsuario.buscarUsuarioPor(id);
        ModelMap model = new ModelMap();

        model.addAttribute("usuario", usuario);

        return new ModelAndView("misClubs", model);

    }
}