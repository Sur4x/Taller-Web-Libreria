package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;

@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;
    @ManyToMany // Relación de muchos a muchos
    @JoinTable(
            name = "libro_genero", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "libro_id"), // Columna para la tabla Libro
            inverseJoinColumns = @JoinColumn(name = "genero_id") // Columna para la tabla Genero
    )
    private List<Genero> generos;
    private Integer cantidadValoraciones;
    private Double promedioRating;
    private String portada; //Imagen

    public Libro(){}

    public Libro(Long id, String autor, String titulo, List<Genero> generos, Integer cantidadValoraciones, Double promedioRating, String portada) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.generos = generos;
        this.cantidadValoraciones = cantidadValoraciones;
        this.promedioRating = promedioRating;
        this.portada = portada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<Genero> getGenero() {
        return generos;
    }

    public void setGenero(List<Genero> generos) {
        this.generos = generos;
    }

    public Integer getCantidadValoraciones() {
        return cantidadValoraciones;
    }

    public void setCantidadValoraciones(Integer cantidadValoraciones) {
        this.cantidadValoraciones = cantidadValoraciones;
    }

    public Double getPromedioRating() {
        return promedioRating;
    }

    public void setPromedioRating(Double promedioRating) {
        this.promedioRating = promedioRating;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }
}
