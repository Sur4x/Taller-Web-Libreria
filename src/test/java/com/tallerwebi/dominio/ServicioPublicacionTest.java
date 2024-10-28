package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioPublicacionTest {

    private RepositorioPublicacion repositorioPublicacionMock;
    private ServicioPublicacion servicioPublicacion;

    @BeforeEach
    public void init(){
        repositorioPublicacionMock = mock(RepositorioPublicacionImpl.class);
        this.servicioPublicacion = new ServicioPublicacionImpl(repositorioPublicacionMock);
    }

    @Test
    public void dadoElMetodoBuscarPublicacionPorIdCuandoLeEntregoUnIdQueEstaEnElSistemaMeDevuelveLaPublicacion(){
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(repositorioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(publicacion);

        Publicacion publicacionEncontrada = servicioPublicacion.buscarPublicacionPorId(1L);

        assertThat(publicacionEncontrada, equalTo(publicacion));
    }

    @Test
    public void dadoElMetodoBuscarPublicacionPorIdCuandoLeEntregoUnIdQueNoEstaEnElSistemaMeDevuelveLaPublicacionVacia(){
        Publicacion publicacion = new Publicacion();
        publicacion.setId(1L);
        when(repositorioPublicacionMock.buscarPublicacionPorId(any())).thenReturn(null);

        Publicacion publicacionEncontrada = servicioPublicacion.buscarPublicacionPorId(1L);

        assertThat(publicacionEncontrada, equalTo(null));
    }

}
