package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioClub {

    Boolean searchClub(Club club);
    Boolean addClub(Club club);

    List<Club> obtenerTodosLosClubs();

    Club buscarClubPor(Long id);
}

