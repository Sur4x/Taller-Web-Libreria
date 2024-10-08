package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioClub {

    Boolean searchClub(Club club);

    List<Club> obtenerTodosLosClubs();

    Club buscarClubPor(Long id);


    void guardar(Club club);

    List<Club> buscarClubPorNombre(String nombre);
}

