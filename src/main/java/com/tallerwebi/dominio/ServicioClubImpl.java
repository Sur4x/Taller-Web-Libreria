package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioClub")
@Transactional
public class ServicioClubImpl implements ServicioClub {

    private RepositorioClub repositorioClub;

    @Autowired
    public ServicioClubImpl(RepositorioClub repositorioClub){
        this.repositorioClub = repositorioClub;
    }

    @Override
    public List<Club> obtenerTodosLosClubes() {
        return repositorioClub.obtenerTodosLosClubes();
    }

    /*
    @Override
    public Club agregarClub(Club club) throws ClubExistente{

        Club clubExistente = repositorioClub.buscarClub(club.getId(), club.getNombre());
        if (clubExistente != null) {
            throw new ClubExistente();
        }
        repositorioClub.guardar(club);
        return club;
    }
*/
    @Override
    public List<Club> buscarClubPorNombre(String nombre) throws NoExisteEseClub {
        return repositorioClub.buscarClubPorNombre(nombre);
    }

    @Override
    public Club actualizarClub(Club club) throws NoExisteEseClub {
        if (club != null){
            repositorioClub.modificar(club);
            return club;
        }else{
            throw new NoExisteEseClub();
        }
    }

    @Override
    public List<Club> buscarClubesPorNombre(String query) {
        return List.of();
    }
}
