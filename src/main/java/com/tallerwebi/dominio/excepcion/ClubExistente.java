package com.tallerwebi.dominio.excepcion;

public class ClubExistente extends Exception{

    public ClubExistente(){
        super("Ya existe un club con ese nombre");
    }
}

