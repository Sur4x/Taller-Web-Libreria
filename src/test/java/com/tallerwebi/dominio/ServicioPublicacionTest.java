package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        publicacion.setComentarios(new ArrayList<>());

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

    @Test
    public void dadoElMetodobuscarPublicacionEnUnClubSiLaEncuentraEnElSistemaLaRetorna(){
        Long publicacionId = 1L;
        Club club = new Club();
        Publicacion publicacion = new Publicacion();
        publicacion.setId(publicacionId);

        when(repositorioPublicacionMock.buscarPublicacionEnUnClub(publicacionId, club)).thenReturn(publicacion);

        Publicacion publicacionObtenida = servicioPublicacion.buscarPublicacionEnUnClub(publicacionId, club);
        assertThat(publicacionObtenida, equalTo(publicacion));
        assertThat(publicacionObtenida.getId(), equalTo(publicacion.getId()));
        verify(repositorioPublicacionMock, times(1)).buscarPublicacionEnUnClub(publicacionId, club);
    }

    @Test
    public void dadoElMetodobuscarPublicacionEnUnClubSiNoLaEncuentraEnElSistemaRetornaNull(){
        Long publicacionId = 1L;
        Club club = new Club();

        when(repositorioPublicacionMock.buscarPublicacionEnUnClub(publicacionId, club)).thenReturn(null);

        Publicacion publicacionObtenida = servicioPublicacion.buscarPublicacionEnUnClub(publicacionId, club);
        assertThat(publicacionObtenida, equalTo(null));
    }

}
