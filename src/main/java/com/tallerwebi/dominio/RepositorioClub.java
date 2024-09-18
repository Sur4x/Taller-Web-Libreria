package com.tallerwebi.dominio;

public interface RepositorioClub {
    Club buscarClub(Long id, String nombre);
    void guardar(Club club);
    Usuario buscarUsuarioEnClub(String nombreClub, String emailUsuario); //setear bien la busqueda
    void modificar(Club club);
}
