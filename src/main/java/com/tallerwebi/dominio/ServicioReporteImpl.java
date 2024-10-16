package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ReporteExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioReporteImpl implements ServicioReporte{
    @Autowired
    private RepositorioReporte repositorioReporte;

    @Override
    public Reporte buscarReportePorId(Long id) {
        Reporte reporte = repositorioReporte.buscarReportePorId(id);
        return reporte;
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
        repositorioReporte.eliminar(reporte);
    }
}
