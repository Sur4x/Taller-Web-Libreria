package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    private String evento;

    public Notificacion(){

    }

    public Notificacion(Long id, LocalDate fecha, String evento) {
        this.id = id;
        this.fecha = fecha;
        this.evento = evento;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

}
