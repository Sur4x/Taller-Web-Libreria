package com.tallerwebi.punta_a_punta.vistas;
import com.microsoft.playwright.Page;

public class VistaHome extends VistaWeb {

    public VistaHome(Page page) {
        super(page);
        page.navigate("localhost:8080/club/home");

    }

    public void darClickEnCrearNuevoClub(){
        this.darClickEnElElemento("div a.btn.btn-primary.me-2");
    }

    public void darClickEnElLogo(){
        this.darClickEnElElemento("nav div a.navbar-brand");
    }

    public void escribirEnElBuscador(String buscar){
        escribirEnElElemento("form input.form-control.me-2" ,buscar);
    }

    public void hacerClickEnElBotonBuscar(){
        this.darClickEnElElemento("form button.btn.btn-outline-light");
    }

    public String obtenerTextoClubsDeTerror(){
        return this.obtenerTextoDelElemento("#clubTerror");
    }

    public void hacerClickEnElClub1(){
        this.darClickEnElElemento("#club-1");
    }

}