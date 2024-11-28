package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Logro;
import com.tallerwebi.dominio.ServicioLogro;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/logros")
public class ControladorLogro {

    @Autowired
    private ServicioLogro servicioLogro;

    @RequestMapping("/actual")
    public String mostrarLogro(HttpServletRequest request, ModelMap model) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Logro logro = servicioLogro.buscarLogroActivo(usuario.getId());

        model.put("logro", logro);
        model.put("logrosLibrosCumplidos", servicioLogro.contarLogrosCumplidosPorTipo(usuario.getId(), "LIBROS"));
        model.put("logrosClubesCumplidos", servicioLogro.contarLogrosCumplidosPorTipo(usuario.getId(), "CLUBES"));

        return "perfil";
    }

    @RequestMapping("/cumplido")
    public String marcarLogroCumplido(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Logro logro = servicioLogro.buscarLogroActivo(usuario.getId());
        servicioLogro.marcarLogroCumplido(logro);

        return "redirect:/logros/actual";
    }

    @RequestMapping("/nuevo")
    public String generarNuevoLogro(HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        servicioLogro.generarLogroAleatorio(usuario);

        return "redirect:/logros/actual";
    }
}
