package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExistenClubs;

import java.util.List;

public interface ServicioClub {

    Boolean agregar(Club club) throws ClubExistente;

    List<Club> obtenerTodosLosClubs() throws NoExistenClubs;

    Club buscarClubPor(Long id) throws NoExisteEseClub;

    // Método para buscar clubes por nombre o coincidencia parcial
    List<Club> buscarClubPorNombre(String nombre);
}
