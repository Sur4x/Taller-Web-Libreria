package com.tallerwebi.dominio;

import org.hibernate.mapping.List;

import javax.persistence.*;
import java.util.Set;


@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String rol;
    private Boolean activo = false;

    @ManyToMany
    @JoinTable(
            name = "user_clubs", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "users_id"), // FK a la tabla de usuarios
            inverseJoinColumns = @JoinColumn(name = "clubs_id") // FK a la tabla de clubes
    )
    private Set<Club> club;


    public Set<Club> getClub() {
        return club;
    }

    public void setClub(Set<Club> club) {
        this.club = club;
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
