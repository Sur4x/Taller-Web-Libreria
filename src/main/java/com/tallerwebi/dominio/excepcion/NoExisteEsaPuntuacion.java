package com.tallerwebi.dominio.excepcion;

public class NoExisteEsaPuntuacion extends RuntimeException {
    public NoExisteEsaPuntuacion(String message) {
        super(message);
    }
}
