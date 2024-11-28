package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioReporte {

    Reporte buscarReportePorId(Long idReporte);

    void guardar(Reporte reporte);

    void eliminar(Reporte reporte);

    Integer obtenerCantidadDeReportesDeUnClub(Long idClub);

    Integer incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(Long idClub);

    Boolean comprobarSiElUsuarioReportoPreviamente(Long idUsuario, Long idClub);

    List<Reporte> obtenerTodosLosReportesDeUnClub(Long id);

    List<Reporte> obtenerTodosLosReportesAprobadosDeUnClub(Long id);
}
