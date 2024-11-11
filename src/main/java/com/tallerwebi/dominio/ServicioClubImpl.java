package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ServicioClubImpl implements ServicioClub {

    private RepositorioClub repositorioClub;
    private RepositorioPublicacion repositorioPublicacion;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioReporte repositorioReporte;
    @Autowired
    private RepositorioNotificacion repositorioNotificacion;

    @Autowired
    public ServicioClubImpl(RepositorioClub repositorioClub, RepositorioPublicacion repositorioPublicacion, RepositorioUsuario repositorioUsuario, RepositorioReporte repositorioReporte) {
        this.repositorioClub = repositorioClub;
        this.repositorioPublicacion = repositorioPublicacion;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioReporte = repositorioReporte;
    }

    @Override
    public Boolean agregar(Club club) throws ClubExistente {
        Club clubExistente = repositorioClub.buscarClubPor(club.getId());
        if (clubExistente != null) {
            throw new ClubExistente("El club ya existe");
        }
        repositorioClub.guardar(club);

        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDate.now());
        notificacion.setEvento("Se creo un nuevo club: " + club.getNombre());

        repositorioNotificacion.crearNotificacion(notificacion);

        return true;
    }

    @Override
    public List<Club> obtenerTodosLosClubs() throws NoExistenClubs {
        List<Club> clubs = repositorioClub.obtenerTodosLosClubs();
        if (clubs.isEmpty()) {
            throw new NoExistenClubs("No existen clubs registrados");
        }
        return clubs;
    }

    @Override
    public Club buscarClubPor(Long id) throws NoExisteEseClub {
        Club club = repositorioClub.buscarClubPor(id);
        if (club == null) {
            throw new NoExisteEseClub("No existe un club con ese ID");
        }
        Hibernate.initialize(club.getPuntuaciones());
        Hibernate.initialize(club.getPublicaciones());
        return club;
    }

    @Override
    public List<Club> buscarClubPorNombre(String nombre) {
        return repositorioClub.buscarClubPorNombre(nombre);
    }

    @Override
    public void registrarUsuarioEnElClub(Usuario usuario, Club club){
        if (usuario != null && club != null) {
            if (!usuario.getClubsInscriptos().contains(club)){
                usuario.getClubsInscriptos().add(club);
                club.getIntegrantes().add(usuario);
                club.setCantidadMiembros(club.getCantidadMiembros() + 1);
                repositorioUsuario.guardar(usuario);
                repositorioClub.guardar(club);
            }
        }
    }
    @Override
    public void borrarRegistroUsuarioEnElClub(Usuario usuario, Club club){
        if (usuario != null && club != null) {
            usuario.getClubsInscriptos().remove(club);
            club.getIntegrantes().remove(usuario);
            club.setCantidadMiembros(club.getCantidadMiembros() - 1);
            repositorioUsuario.guardar(usuario);
            repositorioClub.guardar(club);
        }
    }

    @Override
    public void eliminarClub(Club club) throws NoExisteEseClub {
        if (club == null) {
            throw new NoExisteEseClub("No existe un club con el ID proporcionado.");
        }
        for(Usuario usuario : club.getIntegrantes()){
            usuario.getClubsInscriptos().remove(club);
            repositorioUsuario.guardar(usuario);
        }
        club.getIntegrantes().clear();
        repositorioClub.guardar(club);

        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(LocalDate.now());
        notificacion.setEvento("Se elimino un club existente: " + club.getNombre());

        repositorioNotificacion.crearNotificacion(notificacion);

        repositorioClub.eliminar(club);
    }

    @Override
    public void agregarNuevaPublicacion(Publicacion publicacion, Long id) throws NoExisteEseClub {

        if (publicacion != null && id != null){
            Club club = buscarClubPor(id);
            publicacion.setClub(club);
            club.getPublicaciones().add(publicacion);

            repositorioPublicacion.guardar(publicacion);
            repositorioClub.guardar(club);
        }
    }

    @Override
    public void eliminarPublicacion(Publicacion publicacion, Club club){
        if (publicacion != null && club != null) {
            club.getPublicaciones().remove(publicacion);
            repositorioClub.guardar(club);
            repositorioPublicacion.eliminar(publicacion);
        }
    }

    @Override
    public void obtenerTodosLosReportesDeUnClub(Club club) {

        List<Reporte> listaReportes = repositorioReporte.obtenerTodosLosReportesDeUnClub(club);

        if(listaReportes.size() >= 2){
            club.setEstaReportado("CLUB REPORTADO");
        }else{
            club.setEstaReportado("CLUB ACCESIBLE");
        }
        repositorioClub.guardar(club);
    }

    @Override
    public void agregarNuevoReporteAlClub(Long idClub, Reporte reporte) throws ReporteExistente, NoExisteEseClub {

        Club club = buscarClubPor(idClub); //si el club existe
        if(repositorioReporte.buscarReportePorId(reporte.getId()) != null){ //si el reporte existe
                throw new ReporteExistente("El reporte ya existe");
        }
        reporte.setClub(club);
        club.getReportes().add(reporte);
        repositorioReporte.guardar(reporte);
    }

    public void incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(Long idClub){
       repositorioClub.incrementarCantidadDeReportesEnUnClubObteniendoSuCantidadTotalDeReportes(idClub);
    }

    @Override
    public List<Club> obtenerClubsConMasMiembros() {
        List<Club> clubs = repositorioClub.obtenerClubsConMasMiembros();
        return clubs;
    }

    @Override
    public List<Club> obtenerClubsConMejorPuntuacion() {
        List<Club> clubs = repositorioClub.obtenerClubsConMejorPuntuacion();
        return clubs;
    }

    @Override
    public List<Club> obtenerClubsConMasPublicaciones() {
        List<Club> clubs = repositorioClub.obtenerTodosLosClubs();
        clubs.sort(Comparator.comparingInt(s -> s.getPublicaciones().size()));
        Collections.reverse(clubs);
        List<Club> clubsConMasPublicaciones = new ArrayList<>();

        for(int i=0;i<5 && i < clubs.size();i++){
            clubsConMasPublicaciones.add(clubs.get(i));
        }

        return clubsConMasPublicaciones;
    }

    public Puntuacion buscarPuntuacion(Club club, Usuario usuario){
        return repositorioClub.buscarPuntuacion(club, usuario);
    }

    @Override
    public void agregarPuntuacion(Club club, Usuario usuario, Integer puntuacion) {
        Puntuacion puntuacionClub = repositorioClub.buscarPuntuacion(club, usuario);
        if(puntuacionClub!=null){
            puntuacionClub.setPuntuacion(puntuacion);
        }else {
            puntuacionClub = new Puntuacion(club, usuario, puntuacion);
            club.getPuntuaciones().add(puntuacionClub);
            usuario.getPuntuaciones().add(puntuacionClub);
        }
        repositorioClub.guardarPuntuacion(puntuacionClub);
    }
/* DESPUNTUAR CLUB
    @Override
    public void removerPuntuacion(Club club, Usuario usuario) {
        Puntuacion puntuacionClub = repositorioClub.buscarPuntuacion(club, usuario);
        if(puntuacionClub!=null){
            club.getPuntuaciones().remove(puntuacionClub);
            usuario.getPuntuaciones().remove(puntuacionClub);
            repositorioClub.eliminarPuntuacion(puntuacionClub);

            Double puntuacionPromedio = actualizarPuntuacionPromedio(club);
            actualizarPromedio(club, puntuacionPromedio);

            repositorioClub.guardar(club);
        }
        else throw new NoExisteEsaPuntuacion("No puntuaste aún este club.");
    }

 */

    @Override
    public Double obtenerPuntuacionPromedio(Club club){
        Double puntuacionTotal = 0.0;
        Double puntuacionPromedio = 0.0;
        for(Puntuacion puntuacion : club.getPuntuaciones()){
            puntuacionTotal += puntuacion.getPuntuacion();
        }
        if(!club.getPuntuaciones().isEmpty()){
            puntuacionPromedio = puntuacionTotal / club.getPuntuaciones().size();
        }
        return puntuacionPromedio;
    }


    /*
    @Override
    public void actualizarPromedio(Club club, Double promedio){
        repositorioClub.actualizarPromedio(club.getId(), promedio);
    }

 */

    @Override
    public void actualizarPromedio(Club club, Double promedio){
        club.setPuntuacionPromedio(promedio);
        repositorioClub.guardar(club);
    }
}
