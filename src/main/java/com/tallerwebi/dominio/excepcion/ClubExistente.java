package com.tallerwebi.dominio.excepcion;

public class ClubExistente extends Exception{

    public ClubExistente(String elClubYaExiste){
        super("Ya existe un club con ese nombre");
    }
}

