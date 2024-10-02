package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(nullable = false, lenght = 30) NO PUEDE SER NULO y MAXIMO 30 CARACTERES
    private String nombre;
    private String descripcion;
    private String genero;
    //private Libro libro; AGREGAR ESTE ATRIBUTO
    private String imagen;

    @ManyToMany(mappedBy = "clubsInscriptos", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Usuario> integrantes = new ArrayList<>();

    public Club(){}
    public Club(Long id, String nombre, String descripcion, String genero, String imagen, List<Usuario> integrantes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.genero = genero;
        this.imagen = imagen;
        this.integrantes = integrantes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Usuario> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Usuario> integrantes) {
        this.integrantes = integrantes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Club club = (Club) o;

        return id != null ? id.equals(club.id) : club.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

