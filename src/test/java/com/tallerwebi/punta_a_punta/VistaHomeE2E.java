package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaHome;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaHomeE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaHome vistaHome;
    VistaLogin vistaLogin;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        //browser = playwright.chromium().launch();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(700));
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        Page page = context.newPage();
        vistaLogin = new VistaLogin(page);
        vistaLogin.escribirEMAIL("asd@asd");
        vistaLogin.escribirClave("asd");
        vistaLogin.darClickEnIniciarSesion();
        vistaHome = new VistaHome(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void cuandoElUsuarioHaceClickEnElBotonDelLogoLoDirigeALaVistaHome() {
        vistaHome.darClickEnElLogo();

        String url = vistaHome.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/club/home"));
    }

    @Test
    void cuandoElUsuarioHaceClickEnElBotonCrearNuevoClubLoDirigeALaVistaCrearNuevoClub() {
        vistaHome.darClickEnCrearNuevoClub();
        String url = vistaHome.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/crearClub"));
    }

    @Test
    void cuandoElUsuarioBuscaUnClubConUnNombreEspecificoCambiaLaUrl() {
        vistaHome.escribirEnElBuscador("asd");
        vistaHome.hacerClickEnElBotonBuscar();
        String url = vistaHome.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/club/buscar?query=asd"));
    }

    @Test
    void enElHomeDebeHaberUnSubtituloConClubsDeTerror() {
        String texto = vistaHome.obtenerTextoClubsDeTerror();
        assertThat("Explora nuestros clubs de Terror", equalToIgnoringCase(texto));
    }

    @Test
    void enElHomeDebeHaberUnClubQueAlHacerClickMeRedireccioneAVistaClub() {
        vistaHome.hacerClickEnElClub1();
        String url = vistaHome.obtenerURLActual();
        String clubId = url.replaceAll(".*/club/(\\d+)","$1");
        assertThat(url, containsStringIgnoringCase("/club/" + clubId));
    }

}