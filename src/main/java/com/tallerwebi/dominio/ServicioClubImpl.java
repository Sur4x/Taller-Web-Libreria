package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioClubImpl implements ServicioClub {

    private RepositorioClub repositorioClub;
    private RepositorioPublicacion repositorioPublicacion;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioReporte repositorioReporte;

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
                repositorioUsuario.guardar(usuario);
            }
        }
    }

    @Override
    public void borrarRegistroUsuarioEnElClub(Usuario usuario, Club club){
        if (usuario != null && club != null) {
            usuario.getClubsInscriptos().remove(club);
            club.getIntegrantes().remove(usuario);
            repositorioUsuario.guardar(usuario);
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
}
