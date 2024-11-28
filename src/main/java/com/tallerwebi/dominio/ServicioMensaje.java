package com.tallerwebi.dominio;

public interface ServicioMensaje {

    void guardar(Mensaje mensaje);
    Mensaje setearMensaje(String texto, Usuario usuario, Evento evento);
}
