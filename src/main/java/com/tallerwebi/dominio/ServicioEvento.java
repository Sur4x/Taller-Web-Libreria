package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

public interface ServicioEvento {
    Boolean guardar(Evento evento);

    Evento obtenerEvento(Club club);

    Evento setearEvento(String nombre, String descripcion, LocalDateTime fechaYHora, Club club);

    void actualizarEventosPendientes();

    Evento buscarEventoPorId(Long id);

    List<Mensaje> obtenerMensajesDeUnEvento(Evento evento);

    void finalizarEvento(Evento evento);
}
