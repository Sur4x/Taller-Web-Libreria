package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ServicioMensajeImpl implements ServicioMensaje{

    private RepositorioMensaje repositorioMensaje;

    @Autowired
    public ServicioMensajeImpl(RepositorioMensaje repositorioMensaje) {
        this.repositorioMensaje = repositorioMensaje;
    }

    @Override
    public void guardar(Mensaje mensaje) {
        repositorioMensaje.guardar(mensaje);
    }

    @Override
    public Mensaje setearMensaje(String texto, Usuario usuario, Evento evento) {
        Mensaje mensaje = new Mensaje();
        mensaje.setTexto(texto);
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRemitente(usuario);
        mensaje.setEvento(evento);
        return mensaje;
    }
}
