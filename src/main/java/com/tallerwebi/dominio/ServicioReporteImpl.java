package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.ReporteExistente;
import com.tallerwebi.dominio.excepcion.YaExisteUnReporteDeEsteUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioReporteImpl implements ServicioReporte{

    private RepositorioReporte repositorioReporte;
    private RepositorioClub repositorioClub;
    private RepositorioNotificacion repositorioNotificacion;

    @Autowired
    public ServicioReporteImpl(RepositorioReporte repositorioReporte,RepositorioClub repositorioClub,RepositorioNotificacion repositorioNotificacion) {
        this.repositorioReporte = repositorioReporte;
        this.repositorioClub = repositorioClub;
        this.repositorioNotificacion = repositorioNotificacion;
    }

    @Override
    public Reporte buscarReportePorId(Long id) {
        return repositorioReporte.buscarReportePorId(id);
    }

    @Override
    public void guardarReporte(Reporte reporte) {
        repositorioReporte.guardar(reporte);
    }

    @Override
    public void eliminarReporte(Long id) {
        Reporte reporte = repositorioReporte.buscarReportePorId(id);
        if (reporte != null) {

            repositorioReporte.eliminar(reporte);
        }
    }

    @Override
    public List<Reporte> obtenerTodosLosReportesDeUnClub(Club club){
        return repositorioReporte.obtenerTodosLosReportesDeUnClub(club.getId());
    }

    @Override
    public List<Reporte> obtenerTodosLosReportesAprobadosDeUnClub(Club club) {

        return repositorioReporte.obtenerTodosLosReportesAprobadosDeUnClub(club.getId());
    }

    //mover a reporte
//    @Override
//    public void incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(Long idClub){
//        repositorioReporte.incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(idClub);
//    }

    @Override
    public Integer obtenerCantidadDeReportesDeUnClub(Club club) {
        return repositorioReporte.obtenerCantidadDeReportesDeUnClub(club.getId());
    }

    @Override
    public void agregarNuevoReporteAlClub(Long idClub, Reporte reporte) throws ReporteExistente, NoExisteEseClub {
        Club club = repositorioClub.buscarClubPor(idClub);

        if (club == null) {
            throw new NoExisteEseClub("No existe un club con el id proporcionado");
        }

        if (repositorioReporte.buscarReportePorId(reporte.getId()) != null) {
            throw new ReporteExistente("El reporte ya existe");
        }

        reporte.setClub(club);
        club.getReportes().add(reporte);
        repositorioReporte.guardar(reporte);
    }

    public Boolean comprobarSiElUsuarioReportoPreviamente(Long idUsuario, Long idClub) throws YaExisteUnReporteDeEsteUsuario {
        Boolean poseeReporte = repositorioReporte.comprobarSiElUsuarioReportoPreviamente(idUsuario,idClub);

        if (poseeReporte){
            throw new YaExisteUnReporteDeEsteUsuario();
        }
        return false;
    }

    public void setearUsuarioYClubAUnReporte(Reporte reporte, Club club, Usuario usuario){
        reporte.setClub(club);
        reporte.setUsuario(usuario);
        reporte.setEstado("pendiente");
    }

}
