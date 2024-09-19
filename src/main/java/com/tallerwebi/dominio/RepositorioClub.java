package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioClub {
    List<Club> buscarClubPorNombre(String nombre);
    void guardar(Club club);
    Usuario buscarUsuarioEnClub(String nombreClub, String emailUsuario); //setear bien la busqueda
    void modificar(Club club);
    List<Club> obtenerTodosLosClubes();


}
