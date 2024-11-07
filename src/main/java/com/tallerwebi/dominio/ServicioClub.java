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

    //void agregarNuevaPublicacion(Publicacion publicacion, Long id, Usuario usuario) throws NoExisteEseClub;
    void agregarNuevaPublicacion(Publicacion publicacion, Long id) throws NoExisteEseClub;

    void eliminarPublicacion(Publicacion publicacion, Club club);

    void borrarRegistroUsuarioEnElClub(Usuario usuario, Club club);

    void eliminarClub(Club club) throws NoExisteEseClub;

    void agregarNuevoReporteAlClub(Long id, Reporte reporte) throws ReporteExistente, NoExisteEseClub;

    void obtenerTodosLosReportesDeUnClub(Club club);

    void incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(Long idClub);

    List<Club> obtenerClubsConMasMiembros();

    List<Club> obtenerClubsConMejorPuntuacion();

    List<Club> obtenerClubsConMasPublicaciones();

    void agregarPuntuacion(Club club,Usuario usuario, Integer puntuacion);

    Puntuacion buscarPuntuacion(Club club, Usuario usuario);

    void removerPuntuacion(Club club, Usuario usuario);

    Double actualizarPuntuacionPromedio(Club club);

    void actualizarPromedio(Club club, Double promedio);
}
