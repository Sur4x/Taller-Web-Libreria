package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
//import com.tallerwebi.dominio.excepcion.YaExisteUnReporteDeEsteUsuario;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorReporte {

    private ServicioClub servicioClub;
    private ServicioReporte servicioReporte;
    private ServicioUsuario servicioUsuario;
    private ServicioPuntuacion servicioPuntuacion;
    private ServicioNotificacion servicioNotificacion;

    @Autowired
    public ControladorReporte(ServicioClub servicioClub, ServicioReporte servicioReporte, ServicioUsuario servicioUsuario, ServicioPuntuacion servicioPuntuacion, ServicioNotificacion servicioNotificacion){
        this.servicioClub = servicioClub;
        this.servicioReporte = servicioReporte;
        this.servicioUsuario = servicioUsuario;
        this.servicioPuntuacion = servicioPuntuacion;
        this.servicioNotificacion = servicioNotificacion;
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

    @RequestMapping(path = "/club/{idClub}/listarReportes/aprobarReporte/{idReporte}")
    public ModelAndView aprobarReporte(@PathVariable("idClub") Long idClub, @PathVariable("idReporte") Long idReporte, HttpServletRequest request) throws NoExisteEseClub, NoExisteEseUsuario, YaExisteUnClubConEseNombre, ClubExistente {

        servicioReporte.aprobarReporte(idReporte);

        Reporte reporte = servicioReporte.buscarReportePorId(idReporte);
        Club club = servicioClub.buscarClubPor(idClub);
        servicioNotificacion.enviarNotificacionDeReporteAprobadoALosAdminDelClub(idClub, reporte);

        List<Reporte> reportesAprobados = servicioReporte.obtenerTodosLosReportesAprobadosDeUnClub(club);

        if (reportesAprobados.size() < 3){
            servicioNotificacion.crearNotificacionReporteAprobado(club, reporte);
        }

        if (reportesAprobados.size() >= 3){
            for (Usuario integrante : club.getIntegrantes()) {
                Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPor(integrante.getId());
                servicioNotificacion.crearNotificacion(usuarioEncontrado, "clubEliminado", club.getNombre());
            }

            for (Usuario admin : club.getAdminsSecundarios()) {
                //estoy eliminando el club de los admins
                admin.getClubsAdminSecundarios().remove(club);
                //me falta eliminar los admins del club
            }

            club.setAdminPrincipal(null);

            //servicioClub.actualizar(club);

            servicioClub.eliminarClub(club);

            return new ModelAndView("redirect:/home");
        }

        return new ModelAndView("redirect:/club/" + idClub + "/listarReportes");
    }

    @RequestMapping(path = "/club/{idClub}/listarReportes/rechazarReporte/{idReporte}")
    public ModelAndView rechazarReporte(@PathVariable("idClub") Long idClub, @PathVariable("idReporte") Long idReporte) {

        servicioReporte.eliminarReporte(idReporte);

        return new ModelAndView("redirect:/club/" + idClub + "/listarReportes");
    }
}
