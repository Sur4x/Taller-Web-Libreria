package com.tallerwebi.dominio;

public interface ServicioPuntuacion {

    void agregarPuntuacion(Club club,Usuario usuario, Integer puntuacion);

    Double obtenerPuntuacionPromedio(Club club);

    void actualizarPromedio(Club club);

    Puntuacion buscarPuntuacion(Club club, Usuario usuario);

    void removerPuntuacion(Club club, Usuario usuario);
}
