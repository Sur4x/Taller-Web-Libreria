package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;

import java.util.List;

public interface ServicioClub {

    Boolean agregar(Club club) throws ClubExistente;

    List<Club> obtenerTodosLosClubs() throws NoExistenClubs;

    Club buscarClubPor(Long id) throws NoExisteEseClub;

    // Método para buscar clubes por nombre o coincidencia parcial
    List<Club> buscarClubPorNombre(String nombre);

    void registrarUsuarioEnElClub(Usuario usuario, Club club);

    void borrarRegistroUsuarioEnElClub(Usuario usuario, Club club);

    void eliminarClub(Club club) throws NoExisteEseClub;

    List<Club> obtenerClubsConMasMiembros();

    List<Club> obtenerClubsConMejorPuntuacion();

    List<Club> obtenerClubsConMasPublicaciones();

    void refrescarClub(Club club);

    Boolean usuarioInscriptoEnUnClub(Club club, Usuario usuario);

    void echarUsuarioDeUnClub(Club club, Usuario usuario);
}
