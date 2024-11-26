package com.tallerwebi.dominio.excepcion;

public class YaExisteUnClubConEseNombre extends Exception {
    public YaExisteUnClubConEseNombre() {
        super("Ya existe un club con ese nombre, intente otro.");
    }
}
