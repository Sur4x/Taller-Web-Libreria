package com.tallerwebi.dominio.excepcion;

public class YaExisteUnReporteDeEsteUsuario extends Exception {
    public YaExisteUnReporteDeEsteUsuario() {
        super("Usted ya tiene un reporte en este club.");
    }
}
