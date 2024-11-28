package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.ReporteExistente;
//import com.tallerwebi.dominio.excepcion.YaExisteUnReporteDeEsteUsuario;
import com.tallerwebi.dominio.excepcion.YaExisteUnReporteDeEsteUsuario;
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
    private ServicioUsuario servicioUsuario;
    private ServicioPuntuacion servicioPuntuacion;

    @Autowired
    public ControladorReporte(ServicioClub servicioClub, ServicioReporte servicioReporte, ServicioUsuario servicioUsuario, ServicioPuntuacion servicioPuntuacion){
        this.servicioClub = servicioClub;
        this.servicioReporte = servicioReporte;
        this.servicioUsuario = servicioUsuario;
        this.servicioPuntuacion = servicioPuntuacion;
    }


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

    @RequestMapping(path = "/club/{clubId}/listarReportes", method = RequestMethod.GET)
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

        List<Reporte> reportes = servicioReporte.obtenerTodosLosReportesDeUnClub(club);
        List<Reporte> reportesAprobados = servicioReporte.obtenerTodosLosReportesAprobadosDeUnClub(club);

        model.put("club", club);
        model.put("usuario", usuario);
        model.put("reportes", reportes);
        model.put("reportesAprobados", reportesAprobados);
        model.put("cantidadDeReportes", servicioReporte.obtenerCantidadDeReportesDeUnClub(club));

        return new ModelAndView("verReportes", model);
    }

    @RequestMapping(path = "/club/{clubId}/nuevoReporte", method = RequestMethod.POST)
    public ModelAndView realizarNuevoReporte(@PathVariable("clubId") Long clubId, @ModelAttribute("reporte") Reporte reporte, HttpServletRequest request) throws NoExisteEseClub, ReporteExistente, YaExisteUnReporteDeEsteUsuario, NoExisteEseUsuario {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        ModelMap model = new ModelMap();

        //Usuario usuarioActual = servicioUsuario.buscarUsuarioPor(usuario.getId());
        Club club = servicioClub.buscarClubPor(clubId);
        Boolean poseeReportePrevio = true;

        model.put("club", club);
        model.put("usuario", usuario);
        model.put("puntuacion", servicioPuntuacion.buscarPuntuacion(club, usuario));

        try{
            poseeReportePrevio = servicioReporte.comprobarSiElUsuarioReportoPreviamente(usuario.getId(), club.getId());
        } catch (YaExisteUnReporteDeEsteUsuario e) {
            model.put("mensaje","Ya reportaste este club anteriormente.");
            return new ModelAndView("detalleClub", model);
        }

        if (club != null && usuario != null && !poseeReportePrevio) {
            servicioReporte.setearUsuarioYClubAUnReporte(reporte, club, usuario);
            servicioReporte.guardarReporte(reporte);

            model.put("mensaje","Reporte enviado exitosamente");
            return new ModelAndView("detalleClub", model);
        } else {
            return new ModelAndView("redirect:/home");
        }
    }

    @RequestMapping(path = "/club/eliminar/reporte/{id}", method = RequestMethod.POST)
    public ModelAndView eliminarReporteDeUnClub(@PathVariable("id") Long id) throws NoExisteEseClub {
        ModelMap modelo = new ModelMap();


        servicioReporte.eliminarReporte(id);
        return new ModelAndView("verReportes", modelo);
    }
}
