package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.ClubReportado;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExistenClubs;

import java.util.List;

public interface ServicioClub {

    Boolean agregar(Club club) throws ClubExistente;

    List<Club> obtenerTodosLosClubs() throws NoExistenClubs;

    Club buscarClubPor(Long id) throws NoExisteEseClub;

    // Método para buscar clubes por nombre o coincidencia parcial
    List<Club> buscarClubPorNombre(String nombre);

    void registrarUsuarioEnElClub(Usuario usuario, Club club);

    //void agregarNuevaPublicacion(Publicacion publicacion, Long id, Usuario usuario) throws NoExisteEseClub;
    void agregarNuevaPublicacion(Publicacion publicacion, Long id) throws NoExisteEseClub;

    void eliminarPublicacion(Publicacion publicacion, Club club);

    void agregarNuevoComentario(Publicacion publicacion, Long idClub, Long idPublicacion) throws NoExisteEseClub;

    void borrarRegistroUsuarioEnElClub(Usuario usuario, Club club);

    void eliminarClub(Long id) throws NoExisteEseClub;

    void reportarClub(Long id) throws Exception;

}
