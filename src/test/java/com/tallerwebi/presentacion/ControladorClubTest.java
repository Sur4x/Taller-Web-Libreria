package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioClub;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Club;
import com.tallerwebi.dominio.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class ControladorClubTest {

    private MockMvc mockMvc;

    @Mock
    private ServicioClub servicioClub;

    @Mock
    private ServicioUsuario servicioUsuario;

    @InjectMocks
    private ControladorClub controladorClub;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuramos un ViewResolver para las pruebas
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");

        // Inicializamos MockMvc con el controlador y el ViewResolver
        mockMvc = MockMvcBuilders.standaloneSetup(controladorClub)
                .setViewResolvers(viewResolver)
                .build();
    }

    // Test para irACrearNuevoClub
    @Test
    void irACrearNuevoClub() throws Exception {
        mockMvc.perform(get("/crearClub"))
                .andExpect(status().isOk())
                .andExpect(view().name("crearClub"))
                .andExpect(model().attributeExists("club"))
                .andExpect(model().attributeExists("usuario"));
    }

    // Test para crearNuevoClub
    @Test
    void crearNuevoClub() throws Exception {
        when(servicioClub.agregar(new Club())).thenReturn(true);

        mockMvc.perform(post("/crearNuevoClub")
                        .param("nombre", "Club Test")
                        .sessionAttr("usuario", new Usuario())) // Simula que el usuario está en la sesión
                .andExpect(redirectedUrl("/home"))
                .andExpect(status().is3xxRedirection());
    }

    // Test para irADetalleClub
    @Test
    void irADetalleClub() throws Exception {
        Club club = new Club();
        club.setId(1L);
        when(servicioClub.buscarClubPor(1L)).thenReturn(club);

        mockMvc.perform(get("/club/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("detalleClub"))
                .andExpect(model().attributeExists("club"))
                .andExpect(model().attribute("club", club))
                .andExpect(model().attributeExists("usuario"));
    }

    // Test para buscarClub
    @Test
    void buscarClub() throws Exception {
        List<Club> clubs = Arrays.asList(new Club(), new Club());
        when(servicioClub.buscarClubPorNombre("Test")).thenReturn(clubs);

        mockMvc.perform(get("/buscar")
                        .param("query", "Test")
                        .sessionAttr("usuario", new Usuario())) // Simula que el usuario está en la sesión
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("clubs"))
                .andExpect(model().attribute("clubs", clubs))
                .andExpect(model().attribute("noResultados", false));
    }

    // Test para buscar cuando no se encuentran clubes
    @Test
    void buscarClubSinResultados() throws Exception {
        when(servicioClub.buscarClubPorNombre("NoExiste")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/buscar")
                        .param("query", "NoExiste")
                        .sessionAttr("usuario", new Usuario())) // Simula que el usuario está en la sesión
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("noResultados", true));
    }
}
