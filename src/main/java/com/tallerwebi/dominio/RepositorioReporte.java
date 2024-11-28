package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioReporte {

    Reporte buscarReportePorId(Long idReporte);

    void guardar(Reporte reporte);

    void eliminar(Reporte reporte);

    List<Reporte> obtenerTodosLosReportesDeUnClub(Club club);

    Integer incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(Long idClub);

    Boolean comprobarSiElUsuarioReportoPreviamente(Long idUsuario, Long idClub);
}
