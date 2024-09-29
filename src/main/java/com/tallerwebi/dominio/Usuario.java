package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreUsuario;
    private String descripcion;
    private String email;
    private String password;
    private String rol; //Ver si cambiar por un ENUM
    private Boolean activo = false;
    @ManyToMany // Relación con géneros preferidos
    @JoinTable(
            name = "usuario_genero_preferido", // Tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private List<Genero> generosPreferidos;
    @ManyToMany // Relación con libros leídos
    @JoinTable(
            name = "usuario_libro_leido", // Tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "libro_id")
    )
    private List<Libro> librosLeidos;
    @ManyToMany(mappedBy = "integrantes")
    private List<Club> clubsInscriptos;

    public Usuario(){};
    public Usuario( Long id, String email,String password, Boolean activo) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rol = "usuario";
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }
}
