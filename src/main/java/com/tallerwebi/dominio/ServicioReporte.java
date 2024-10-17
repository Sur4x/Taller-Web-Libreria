package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ReporteExistente;

public interface ServicioReporte {

    Reporte buscarReportePorId(Long id);

    void guardarReporte(Reporte reporte) throws ReporteExistente;

    void eliminarReporte(Long id);
}
