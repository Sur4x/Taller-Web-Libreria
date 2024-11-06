package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioClub {

    List<Club> obtenerTodosLosClubs();

    Club buscarClubPor(Long id);

    void guardar(Club club);

    List<Club> buscarClubPorNombre(String nombre);

    void eliminar(Club club);

    Integer incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(Long idClub);

    List<Club> obtenerClubsConMasMiembros();

    List<Club> obtenerClubsConMejorCalificacion();
}

