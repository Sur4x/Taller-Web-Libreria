package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

@Controller
public class ControladorUsuario {

    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario){
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping("/busqueda")
    public ModelAndView mostrarUsuarios() throws NoExistenUsuarios {
        List<Usuario> usuarios = servicioUsuario.mostrarTodosLosUsuarios();
        ModelMap modelo = new ModelMap();
        modelo.put("usuarios", usuarios);

        return new ModelAndView("busqueda", modelo);
    }

    @RequestMapping(path = "/perfil/{id}", method = RequestMethod.GET)
    public ModelAndView irAPerfil(@PathVariable("id") Long id) throws NoExisteEseUsuario {
        Usuario usuario = servicioUsuario.buscarUsuarioPor(id);
        ModelMap model = new ModelMap();
        if (usuario != null){
            model.put("usuario",usuario);
            return new ModelAndView("perfil", model);
        }else{
            return new ModelAndView("Redirect: /home", model);
        }
    }
}

