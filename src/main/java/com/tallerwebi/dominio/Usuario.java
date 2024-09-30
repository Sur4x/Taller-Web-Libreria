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

    @Transient //ESTO HACE QUE ESTE ATRIBUTO NO SE GUARDE EN LA BDD
    private String confirmPassword;

    private String rol;

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
    public Usuario(String nombreUsuario, String email,String password) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Genero> getGenerosPreferidos() {
        return generosPreferidos;
    }

    public void setGenerosPreferidos(List<Genero> generosPreferidos) {
        this.generosPreferidos = generosPreferidos;
    }

    public List<Libro> getLibrosLeidos() {
        return librosLeidos;
    }

    public void setLibrosLeidos(List<Libro> librosLeidos) {
        this.librosLeidos = librosLeidos;
    }

    public List<Club> getClubsInscriptos() {
        return clubsInscriptos;
    }

    public void setClubsInscriptos(List<Club> clubsInscriptos) {
        this.clubsInscriptos = clubsInscriptos;
    }
}
