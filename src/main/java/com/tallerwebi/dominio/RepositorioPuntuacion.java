package com.tallerwebi.dominio;

public interface RepositorioPuntuacion {

    Puntuacion buscarPuntuacion(Club club, Usuario usuario);

    void guardarPuntuacion(Puntuacion puntuacionClub);

    void eliminarPuntuacion(Puntuacion puntuacionClub);

    //void actualizarPromedio(Long id, Double promedio);
}
