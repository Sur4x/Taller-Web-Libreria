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
    private Integer cantidadDeReportes;
    private String estaReportado;

    @ManyToMany(mappedBy = "clubsInscriptos", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Usuario> integrantes = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publicacion> publicaciones = new ArrayList<>();

    public Club() {
    }

    public Club(Long id, String nombre, String descripcion, String genero, String imagen, Integer cantidadDeReportes, String estaReportado,List<Usuario> integrantes, List<Publicacion> publicaciones) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.genero = genero;
        this.imagen = imagen;
        this.cantidadDeReportes = cantidadDeReportes;
        this.estaReportado = estaReportado;
        this.integrantes = integrantes;
        this.publicaciones = publicaciones;
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

    public Integer getCantidadDeReportes() {
        return cantidadDeReportes;
    }

    public void setCantidadDeReportes(Integer reportes) {
        this.cantidadDeReportes = reportes;
    }

    public String getEstaReportado() {
        return estaReportado;
    }

    public void setEstaReportado(String estaReportado) {
        this.estaReportado = estaReportado;
    }

    public List<Usuario> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Usuario> integrantes) {
        this.integrantes = integrantes;
    }

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
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

