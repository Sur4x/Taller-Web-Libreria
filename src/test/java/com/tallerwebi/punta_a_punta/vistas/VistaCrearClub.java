package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaCrearClub extends VistaWeb {

    public VistaCrearClub(Page page) {
        super(page);
        page.navigate("localhost:8080/club/crearClub");
    }

    public void seleccionarImagen(String url){
        this.seleccionarImagen("#imagen", url);
    }

    public void escribirNombre(String nombre){
        this.escribirEnElElemento("#nombre", nombre);
    }

    public void escribirDescripcion(String descripcion){
        this.escribirEnElElemento("#descripcion", descripcion);
    }

    public void seleccionarGenero(String genero){
        this.seleccionarGenero("#genero", genero);
    }

    public void darClickEnCrear(){
        this.darClickEnElElemento("#crearClub");
    }
}
