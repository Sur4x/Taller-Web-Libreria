package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.ServicioClub;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ClubController {
    private final ServicioClub servicioClub;


    public ClubController(ServicioClub servicioClub) {
        this.servicioClub = servicioClub;
    }

    @GetMapping("/buscar-club")
    public String buscarClub(@RequestParam("query") String query, Model model) {
        List<Club> clubes = servicioClub.buscarClubesPorNombre(query);
        model.addAttribute("clubes", clubes);
        return "resultado-busqueda"; // Nombre del archivo HTML Thymeleaf
    }
}
