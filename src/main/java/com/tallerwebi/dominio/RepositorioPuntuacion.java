package com.tallerwebi.dominio;

public interface RepositorioPuntuacion {

    Puntuacion buscarPuntuacion(Club club, Usuario usuario);

    void guardarPuntuacion(Puntuacion puntuacionClub);

    void eliminarPuntuacion(Long idClub, Long idUsuario);

    //void actualizarPromedio(Long id, Double promedio);
}
