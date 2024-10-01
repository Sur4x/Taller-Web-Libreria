package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExistenClubs;

import java.util.List;

public interface ServicioClub {

    //public List<Club> listAllClubs();

    public Boolean agregar(Club club) throws ClubExistente;

    public List<Club> obtenerTodosLosClubs() throws NoExistenClubs;

    Club buscarClubPor(Long id) throws NoExisteEseClub;


}

