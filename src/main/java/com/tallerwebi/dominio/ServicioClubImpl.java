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
    private RepositorioPublicacion repositorioPublicacion; //borrar
    private RepositorioUsuario repositorioUsuario;
    private RepositorioReporte repositorioReporte;
    private RepositorioNotificacion repositorioNotificacion;

    @Autowired
    public ServicioClubImpl(RepositorioClub repositorioClub, RepositorioPublicacion repositorioPublicacion, RepositorioUsuario repositorioUsuario, RepositorioReporte repositorioReporte,RepositorioNotificacion repositorioNotificacion) {
        this.repositorioClub = repositorioClub;
        this.repositorioPublicacion = repositorioPublicacion;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioReporte = repositorioReporte;
        this.repositorioNotificacion = repositorioNotificacion;
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
        Hibernate.initialize(club.getAdminsSecundarios());
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
        for (Club club : clubs) {
            Hibernate.initialize(club.getPublicaciones());
        }

        clubs.sort(Comparator.comparingInt(s -> s.getPublicaciones().size()));
        Collections.reverse(clubs);
        List<Club> clubsConMasPublicaciones = new ArrayList<>();

        for(int i=0;i<5 && i < clubs.size();i++){
            clubsConMasPublicaciones.add(clubs.get(i));
        }

        return clubsConMasPublicaciones;
    }

    @Override
    public void refrescarClub(Club club){
        repositorioClub.refrescarClub(club);
    }

    @Override
    public Boolean usuarioInscriptoEnUnClub(Club club, Usuario usuario) {
        return club.getIntegrantes().contains(usuario);
    }

    @Override
    public void echarUsuarioDeUnClub(Club club, Usuario usuario) {

        if (club.getAdminsSecundarios().contains(usuario)){
            club.getAdminsSecundarios().remove(usuario);
            usuario.getClubsAdminSecundarios().remove(club);
        }

        club.getIntegrantes().remove(usuario);
        usuario.getClubsInscriptos().remove(club);

        repositorioClub.guardar(club);
        repositorioUsuario.guardar(usuario);
    }

    @Override
    public void hacerAdminAUnUsuarioDeUnClub(Club club, Usuario nuevoAdmin) {
        club.getAdminsSecundarios().add(nuevoAdmin);
        nuevoAdmin.getClubsAdminSecundarios().add(club);

        repositorioClub.guardar(club);
        repositorioUsuario.guardar(nuevoAdmin);
    }

    @Override
    public void sacarAdminAUnUsuarioDeUnClub(Club club, Usuario usuario) {
        club.getAdminsSecundarios().remove(usuario);
        usuario.getClubsAdminSecundarios().remove(club);

        repositorioClub.guardar(club);
        repositorioUsuario.guardar(usuario);
    }
}
