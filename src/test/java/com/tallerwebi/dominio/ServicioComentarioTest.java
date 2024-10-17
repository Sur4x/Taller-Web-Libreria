package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioComentarioImpl;
import com.tallerwebi.infraestructura.RepositorioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ServicioComentarioTest {

    private RepositorioPublicacion repositorioPublicacionMock;
    private RepositorioComentario repositorioComentarioMock;
    private ServicioComentario servicioComentario;

    @BeforeEach
    public void init(){
        repositorioPublicacionMock = mock(RepositorioPublicacionImpl.class);
        repositorioComentarioMock = mock(RepositorioComentarioImpl.class);
        this.servicioComentario = new ServicioComentarioImpl(repositorioPublicacionMock,repositorioComentarioMock);
    }

    @Test
    public void dadoElMetodoGuardarComentarioCuandoIntentoGuardarUnComentarioLoHaceCorrectamente(){
        Comentario comentario = new Comentario();
        Publicacion publicacion = new Publicacion();
        publicacion.setComentarios(new ArrayList<>());

        servicioComentario.guardarComentario(comentario, publicacion);

        verify(repositorioComentarioMock, times(1)).guardar(comentario);
        verify(repositorioPublicacionMock, times(1)).guardar(publicacion);
    }

    @Test
    public void dadoElMetodoGuardarComentarioCuandoIntentoGuardarUnComentarioNuloNoRealizaLosMetodosCorrespodientes(){
        Comentario comentario = null;
        Publicacion publicacion = null;

        servicioComentario.guardarComentario(comentario, publicacion);

        verify(repositorioComentarioMock, times(0)).guardar(comentario);
        verify(repositorioPublicacionMock, times(0)).guardar(publicacion);
    }
}
