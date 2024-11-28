package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioEvento {
    void guardar(Evento evento);

    Evento buscarEventoValido(Club club);

    List<Evento> buscarEventoPorEstado(String estado);

    Evento buscarEventoPorId(Long id);

    List<Mensaje> buscarComentariosEnUnEvento(Evento evento);

    void finalizar(Evento evento);
}
