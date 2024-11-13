package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioPuntuacionImp implements ServicioPuntuacion{

    private RepositorioPuntuacion repositorioPuntuacion;
    private RepositorioClub repositorioClub;

    @Autowired
    public ServicioPuntuacionImp(RepositorioPuntuacion repositorioPuntuacion, RepositorioClub repositorioClub){
        this.repositorioPuntuacion = repositorioPuntuacion;
        this.repositorioClub = repositorioClub;
    }

    @Override
    public void agregarPuntuacion(Club club, Usuario usuario, Integer puntuacion) {
        Puntuacion puntuacionClub = repositorioPuntuacion.buscarPuntuacion(club, usuario);
        if(puntuacionClub!=null){
            puntuacionClub.setPuntuacion(puntuacion);
        }else {
            puntuacionClub = new Puntuacion(club, usuario, puntuacion);
            club.getPuntuaciones().add(puntuacionClub);
            usuario.getPuntuaciones().add(puntuacionClub);
        }
        repositorioPuntuacion.guardarPuntuacion(puntuacionClub);
    }

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

    @Override
    public void actualizarPromedio(Club club, Double promedio){
        club.setPuntuacionPromedio(promedio);
        repositorioClub.guardar(club);
    }

    @Override
    public Puntuacion buscarPuntuacion(Club club, Usuario usuario){
        return repositorioPuntuacion.buscarPuntuacion(club, usuario);
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

        /*
    @Override
    public void actualizarPromedio(Club club, Double promedio){
        repositorioClub.actualizarPromedio(club.getId(), promedio);
    }

 */

}
