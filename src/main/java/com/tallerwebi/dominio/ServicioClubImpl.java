package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExistenClubs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioClub")
@Transactional
public class ServicioClubImpl implements ServicioClub{

    private RepositorioClub repositorioClub;

    @Autowired
    public ServicioClubImpl(RepositorioClub repositorioClub){
        this.repositorioClub = repositorioClub;
    }

    @Override
    public Boolean addClub(Club club) throws ClubExistente {
        Boolean agregado = false;
        Boolean clubRepetido = repositorioClub.searchClub(club);

        if (!clubRepetido){
            agregado = repositorioClub.addClub(club);
        }else{
            throw new ClubExistente();
        }
        return agregado;

    }

    @Override
    public List<Club> obtenerTodosLosClubs() throws NoExistenClubs {
        List<Club> clubs = repositorioClub.obtenerTodosLosClubs();
        if (!clubs.isEmpty()){
            return clubs;
        }else{
            throw new NoExistenClubs("No existen clubs para mostrar");
        }

    }

    @Override
    public Club buscarClubPor(Long id) throws NoExisteEseClub {
        Club club = repositorioClub.buscarClubPor(id);
        if (club != null){
            return club;
        }else{
            throw new NoExisteEseClub("No existe ningun club con ese id");
        }
    }


}
