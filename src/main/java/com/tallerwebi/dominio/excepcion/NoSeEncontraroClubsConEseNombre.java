package com.tallerwebi.dominio.excepcion;

public class NoSeEncontraroClubsConEseNombre extends Exception {

    public NoSeEncontraroClubsConEseNombre() {
        super("No existe ningun club con ese nombre.");
    }
}
