package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaCrearClub;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaCrearClubE2E {
//
//    static Playwright playwright;
//    static Browser browser;
//    BrowserContext context;
//    VistaLogin vistaLogin;
//    VistaCrearClub vistaCrearClub;
//
//    @BeforeAll
//    static void abrirNavegador() {
//        playwright = Playwright.create();
//        //browser = playwright.chromium().launch();
//        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(700));
//    }
//
//    @AfterAll
//    static void cerrarNavegador() {
//        playwright.close();
//    }
//
//    @BeforeEach
//    void crearContextoYPagina() {
//        context = browser.newContext();
//        Page page = context.newPage();
//        vistaLogin = new VistaLogin(page);
//        vistaLogin.escribirEMAIL("admin@unlam.com.ar");
//        vistaLogin.escribirClave("124");
//        vistaLogin.darClickEnIniciarSesion();
//        vistaCrearClub = new VistaCrearClub(page);
//    }
//
//    @AfterEach
//    void cerrarContexto() {
//        context.close();
//    }
//
//    @Test
//    void cuandoCompletoElFormularioParaCrearClubMeRedirigeAlHome() {
//        vistaCrearClub.seleccionarImagen("C:/Users/sebac/Downloads/jujutsu-kaisen-gojo-cosplay.jpg");
//        vistaCrearClub.escribirNombre("Club Prueba E2E");
//        vistaCrearClub.escribirDescripcion("Descripción de prueba");
//        vistaCrearClub.seleccionarGenero("Terror");
//        vistaCrearClub.darClickEnCrear();
//        String url = vistaCrearClub.obtenerURLActual();
//        String clubId = url.replaceAll(".*/club/(\\d+)","$1");
//        assertThat(url, containsStringIgnoringCase("/club/" + clubId));
//    }
}
