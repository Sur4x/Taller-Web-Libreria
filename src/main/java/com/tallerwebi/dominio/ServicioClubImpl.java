package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ClubExistente;
import com.tallerwebi.dominio.excepcion.NoExisteEseClub;
import com.tallerwebi.dominio.excepcion.NoExistenClubs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicioClubImpl implements ServicioClub {

    @Autowired
    private RepositorioClub repositorioClub;

    @Override
    @Transactional  // Aseguramos que este método esté dentro de una transacción
    public Boolean agregar(Club club) throws ClubExistente {
        Boolean clubExistente = repositorioClub.searchClub(club);
        if (clubExistente) {
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
    @Transactional(readOnly = true)
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
}
