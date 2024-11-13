package com.tallerwebi.dominio;

public interface ServicioPuntuacion {

    void agregarPuntuacion(Club club,Usuario usuario, Integer puntuacion);

    Double obtenerPuntuacionPromedio(Club club);

    void actualizarPromedio(Club club, Double promedio);

    Puntuacion buscarPuntuacion(Club club, Usuario usuario);

    /* DESPUNTUAR CLUB
    void removerPuntuacion(Club club, Usuario usuario);
    */
}
