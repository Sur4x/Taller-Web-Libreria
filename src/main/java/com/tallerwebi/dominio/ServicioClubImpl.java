package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicioClubImpl implements ServicioClub {


    private RepositorioClub repositorioClub;
    @Autowired
    private RepositorioPublicacion repositorioPublicacion;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioReporte repositorioReporte;

    @Autowired
    public ServicioClubImpl(RepositorioClub repositorioClub) {
        this.repositorioClub = repositorioClub;
    }

    @Override
    @Transactional  // Aseguramos que este método esté dentro de una transacción
    public Boolean agregar(Club club) throws ClubExistente {
        Club clubExistente = repositorioClub.buscarClubPor(club.getId());
        if (clubExistente!=null) {
            throw new ClubExistente("El club ya existe");
        }
        repositorioClub.guardar(club);
        return true;
    }

    @Override
    @Transactional(readOnly = true)  // La lectura de datos también debe estar dentro de una transacción
    public List<Club> obtenerTodosLosClubs() throws NoExistenClubs {
        List<Club> clubs = repositorioClub.obtenerTodosLosClubs();
        if (clubs.isEmpty()) {
            throw new NoExistenClubs("No existen clubs registrados");
        }
        return clubs;
    }

    @Override
    @Transactional
    public Club buscarClubPor(Long id) throws NoExisteEseClub {
        Club club = repositorioClub.buscarClubPor(id);

        if (club == null) {
            throw new NoExisteEseClub("No existe un club con ese ID");
        }
        return club;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Club> buscarClubPorNombre(String nombre) {
        return repositorioClub.buscarClubPorNombre(nombre);
    }

    @Override
    @Transactional
    public void registrarUsuarioEnElClub(Usuario usuario, Club club){
        usuario.getClubsInscriptos().add(club);
        club.getIntegrantes().add(usuario);
        repositorioUsuario.guardar(usuario);
    }

    @Override
    @Transactional
    public void borrarRegistroUsuarioEnElClub(Usuario usuario, Club club){
        usuario.getClubsInscriptos().remove(club);
        club.getIntegrantes().remove(usuario);
        repositorioUsuario.guardar(usuario);
    }

    @Override
    @Transactional
    public void eliminarClub(Long id) throws NoExisteEseClub {
        Club club = repositorioClub.buscarClubPor(id);
        if (club == null) {
            throw new NoExisteEseClub("No existe un club con el ID proporcionado.");
        }
        repositorioClub.eliminar(id);
    }

    @Override
    @Transactional
    public void agregarNuevaPublicacion(Publicacion publicacion, Long id) throws NoExisteEseClub {
        Club club = buscarClubPor(id);
        publicacion.setClub(club);
        club.getPublicaciones().add(publicacion);

        repositorioPublicacion.guardar(publicacion);

        repositorioClub.guardar(club);
    }


    @Override
    @Transactional
    public void eliminarPublicacion(Publicacion publicacion, Club club){
        club.getPublicaciones().remove(publicacion);
        repositorioClub.guardar(club);
        repositorioPublicacion.eliminar(publicacion);
    }

    @Override
    @Transactional
    public void agregarNuevoComentario(Publicacion publicacion, Long idclub, Long idPublicacion) throws NoExisteEseClub {

    }

    @Override
    @Transactional
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
    @Transactional
    public void agregarNuevoReporteAlClub(Long idClub, Reporte reporte) throws Exception {
        try{
            Club club = buscarClubPor(idClub);

            if(repositorioReporte.buscarReportePorId(reporte.getId()) != null){
                throw new ReporteExistente("El reporte ya existe");
            }

            reporte.setClub(club);
            club.getReportes().add(reporte);

            repositorioReporte.guardar(reporte);

        } catch (Exception e) {
            throw e;
        }

    }
}
