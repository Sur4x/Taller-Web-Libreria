package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ServicioEventoImpl implements ServicioEvento {

    private RepositorioEvento repositorioEvento;

    @Autowired
    public ServicioEventoImpl(RepositorioEvento repositorioEvento) {
        this.repositorioEvento = repositorioEvento;
    }

    @Override
    public Boolean guardar(Evento evento) {
        Boolean existe = false;
        repositorioEvento.guardar(evento);
        if(evento.getId()!=null){
            existe = true;
        }
        return existe;
    }

    @Override
    public Evento buscarEventoPorId(Long id){
        Evento evento = repositorioEvento.buscarEventoPorId(id);
        return evento;
    }

    @Override
    public List<Mensaje> obtenerMensajesDeUnEvento(Evento evento) {
        return repositorioEvento.buscarComentariosEnUnEvento(evento);
    }

    @Override
    public Evento obtenerEvento(Club club) {
        Evento evento = repositorioEvento.buscarEventoValido(club);
        return evento;
    }

    @Override
    public Evento setearEvento(String nombre, String descripcion, LocalDateTime fechaYHora, Club club) {
        Evento evento = new Evento();
        evento.setNombre(nombre);
        evento.setDescripcion(descripcion);
        evento.setFechaYHora(fechaYHora);
        evento.setClub(club);
        evento.setEstado("pendiente");
        return evento;
    }

    @Override
    @Scheduled(fixedRate = 60000) // Ejecutar cada minuto
    public void actualizarEventosPendientes() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Evento> eventosPendientes = repositorioEvento.buscarEventoPorEstado("Pendiente");

        for (Evento evento : eventosPendientes) {
            if (evento.getFechaYHora().isBefore(ahora) || evento.getFechaYHora().isEqual(ahora)) {
                evento.setEstado("activo");
                repositorioEvento.guardar(evento);
            }
        }
    }

    @Override
    public void finalizarEvento(Evento evento){
        repositorioEvento.finalizar(evento);
    }
}
