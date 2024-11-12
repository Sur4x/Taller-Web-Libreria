package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.nio.file.Paths;

public class VistaWeb {
    protected Page page;

    public VistaWeb(Page page) {
        this.page = page;
    }

    public String obtenerURLActual(){
        return page.url();
    }

    protected String obtenerTextoDelElemento(String selectorCSS){
        return this.obtenerElemento(selectorCSS).textContent();
    }
    protected void darClickEnElElemento(String selectorCSS){
        this.obtenerElemento(selectorCSS).click();
    }

    protected void escribirEnElElemento(String selectorCSS, String texto){
        this.obtenerElemento(selectorCSS).type(texto);
    }

    protected void seleccionarImagen(String selectorCSS, String urlIamgen){
        this.obtenerElemento(selectorCSS).setInputFiles(Paths.get(urlIamgen));
    }

    protected void seleccionarGenero(String selectorCSS, String genero) {
        this.obtenerElemento(selectorCSS).selectOption(genero);
    }

    private Locator obtenerElemento(String selectorCSS){
        return page.locator(selectorCSS);
    }
}
