package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.ReporteExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioReporteImpl implements ServicioReporte{

    private RepositorioReporte repositorioReporte;
    private RepositorioClub repositorioClub;

    @Autowired
    public ServicioReporteImpl(RepositorioReporte repositorioReporte,RepositorioClub repositorioClub) {
        this.repositorioReporte = repositorioReporte;
        this.repositorioClub = repositorioClub;
    }

    @Override
    public Reporte buscarReportePorId(Long id) {
        return repositorioReporte.buscarReportePorId(id);
    }

    @Override
    @Transactional
    public void guardarReporte(Reporte reporte) throws ReporteExistente {
        Reporte reporteAGuardar = repositorioReporte.buscarReportePorId(reporte.getId());

        if(reporteAGuardar != null){
            throw new ReporteExistente("El reporte ya fue realizado");
        }
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
    public List<Reporte> listarReportesPorClub(Club club){
        List<Reporte> reportes = repositorioReporte.obtenerTodosLosReportesDeUnClub(club);
        return reportes != null ? reportes : new ArrayList<>();
    }

    //mover a reporte
    @Override
    public void incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(Long idClub){
        repositorioReporte.incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(idClub);
    }

    @Override
    public void obtenerTodosLosReportesDeUnClub(Club club) {

        List<Reporte> listaReportes = repositorioReporte.obtenerTodosLosReportesDeUnClub(club);

        if(listaReportes.size() >= 2){
            club.setEstaReportado("CLUB REPORTADO");
        }else{
            club.setEstaReportado("CLUB ACCESIBLE");
        }
        repositorioClub.guardar(club);
    }

    //Mover a servicio reporte
    @Override
    public void agregarNuevoReporteAlClub(Long idClub, Reporte reporte) throws ReporteExistente, NoExisteEseClub {
        Club club = repositorioClub.buscarClubPor(idClub); // si el club existe

        if (club == null) { // verificar si el club no existe
            throw new NoExisteEseClub("No existe un club con el id proporcionado");
        }

        if (repositorioReporte.buscarReportePorId(reporte.getId()) != null) { // si el reporte existe
            throw new ReporteExistente("El reporte ya existe");
        }

        reporte.setClub(club);
        club.getReportes().add(reporte);
        repositorioReporte.guardar(reporte);
    }

}
