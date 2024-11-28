package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Mensaje {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String texto;  // Contenido del mensaje
    private LocalDateTime fechaEnvio;  // Fecha en la que se envió el mensaje

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario remitente;

    @ManyToOne
    @JoinColumn(name = "evento_id")  // La columna que referencia al evento
    private Evento evento;

    public Mensaje() {
    }
    // Constructor, getters y setters
    public Mensaje(Long id, String texto, LocalDateTime fechaEnvio, Usuario remitente) {
        this.id = id;
        this.texto = texto;
        this.fechaEnvio = fechaEnvio;
        this.remitente = remitente;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Usuario getRemitente() {
        return remitente;
    }

    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}

