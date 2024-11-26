package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.ReporteExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorReporte {

    private ServicioClub servicioClub;
    private ServicioReporte servicioReporte;

    @Autowired
    public ControladorReporte(ServicioClub servicioClub, ServicioReporte servicioReporte){
        this.servicioClub = servicioClub;
        this.servicioReporte = servicioReporte;
    }

    //esto va en el controlador Reporte
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
    //esto va en el controlador reporte
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

        servicioReporte.obtenerTodosLosReportesDeUnClub(club);

        model.put("club", club);
        model.put("usuario", usuario);
        model.put("reportes", reportes);

        return new ModelAndView("verReportes", model);
    }

    //esto va en el controlador reporte
    @RequestMapping(path = "/club/{clubId}/nuevoReporte", method = RequestMethod.POST)
    public ModelAndView realizarNuevoReporte(@PathVariable("clubId") Long clubId, @ModelAttribute("reporte") Reporte reporte, HttpServletRequest request) throws NoExisteEseClub, ReporteExistente {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        Club club = servicioClub.buscarClubPor(clubId);
        if (club != null && usuario != null) {
            reporte.setClub(club);
            servicioReporte.guardarReporte(reporte);
            servicioReporte.incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(clubId);
            return new ModelAndView("redirect:/club/" + clubId);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    //esto va en el controlador reporte
    @RequestMapping(path = "/club/eliminar/reporte/{id}", method = RequestMethod.POST)
    public ModelAndView eliminarReporteDeUnClub(@PathVariable("id") Long id) throws NoExisteEseClub {
        servicioReporte.eliminarReporte(id);
        return new ModelAndView("redirect:/home");
    }
}
