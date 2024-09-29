package com.tallerwebi.dominio.excepcion;

public class UsuarioExistente extends Exception {
    public UsuarioExistente(){
        super("Ya existe un usuario");
    }
}

