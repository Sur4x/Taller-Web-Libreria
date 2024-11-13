package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.ReporteExistente;

import java.util.List;

public interface ServicioReporte {

    Reporte buscarReportePorId(Long id);

    void guardarReporte(Reporte reporte) throws ReporteExistente;

    void eliminarReporte(Long id);

    List<Reporte> listarReportesPorClub(Club club) throws NoExisteEseClub;

    void incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(Long idClub);

    void agregarNuevoReporteAlClub(Long id, Reporte reporte) throws ReporteExistente, NoExisteEseClub;

    void obtenerTodosLosReportesDeUnClub(Club club);
}
