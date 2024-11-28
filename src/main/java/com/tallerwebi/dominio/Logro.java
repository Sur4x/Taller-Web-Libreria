package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
@Table(name = "logros")
public class Logro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion; // Ejemplo: "Leer 10 libros" o "Unirse a 3 clubes"

    @Column(nullable = false)
    private String tipo; // "LIBROS" o "CLUBES"

    @Column(nullable = false)
    private Boolean cumplido = false; // Si el logro está cumplido o no

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getCumplido() {
        return cumplido;
    }

    public void setCumplido(Boolean cumplido) {
        this.cumplido = cumplido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
