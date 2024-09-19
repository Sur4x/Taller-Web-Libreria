package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;

import java.util.List;

public interface ServicioClub {
    //buscarClub / guardarClub / buscarUsuarioEnClub / modificarClub

    List<Club> obtenerTodosLosClubes();

    //Club agregarClub(Club club) throws ClubExistente;

    List<Club> buscarClubPorNombre(String nombre) throws NoExisteEseClub;

    Club actualizarClub(Club club) throws NoExisteEseClub;
}
