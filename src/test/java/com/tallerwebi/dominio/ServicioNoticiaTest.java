package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioNoticiaTest {

    private ServicioNoticia servicioNoticia;
    private RepositorioNoticia repositorioNoticiaMock;

    @BeforeEach
    public void init(){
        repositorioNoticiaMock = mock(RepositorioNoticia.class);
        this.servicioNoticia = new ServicioNoticiaImpl(repositorioNoticiaMock);
    }

    @Test
    public void dadoElMetodoObtenerNoticiasRandomCuandoExisten2NoticiasEnLaBDDLasRetorna(){
        List<Noticia> noticias = new ArrayList<>();
        noticias.add(new Noticia());
        noticias.add(new Noticia());

        when(repositorioNoticiaMock.obtenerNoticiasRandom(2)).thenReturn(noticias);
        List<Noticia> noticiasObtenidas = servicioNoticia.obtenerNoticiasRandom(2);

        assertThat(noticiasObtenidas.size(),equalTo(2));

    }

}
