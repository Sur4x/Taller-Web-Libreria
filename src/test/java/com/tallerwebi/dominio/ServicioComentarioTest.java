package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioComentarioImpl;
import com.tallerwebi.infraestructura.RepositorioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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

    @Test
    public void dadoElMetodoBuscarComentarioEnUnaPublicacionSiLoEncuentroEnElSistemaRetornaUnComentario(){
        Long comentarioId = 1L;
        Publicacion publicacion = new Publicacion();
        Comentario comentario = new Comentario();

        when(repositorioComentarioMock.buscarComentarioEnUnaPublicacion(comentarioId, publicacion)).thenReturn(comentario);

        Comentario comentarioObtenido = servicioComentario.buscarComentarioEnUnaPublicacion(comentarioId, publicacion);

        assertThat(comentarioObtenido, equalTo(comentario));
        verify(repositorioComentarioMock, times(1)).buscarComentarioEnUnaPublicacion(comentarioId, publicacion);
    }

    @Test
    public void dadoElMetodoBuscarComentarioEnUnaPublicacionSiNoLoEncuentroEnElSistemaRetornaNull(){
        Long comentarioId = 1L;
        Publicacion publicacion = new Publicacion();

        when(repositorioComentarioMock.buscarComentarioEnUnaPublicacion(comentarioId, publicacion)).thenReturn(null);

        Comentario comentarioObtenido = servicioComentario.buscarComentarioEnUnaPublicacion(comentarioId, publicacion);

        assertThat(comentarioObtenido, equalTo(null));
        verify(repositorioComentarioMock, times(1)).buscarComentarioEnUnaPublicacion(comentarioId, publicacion);
    }

    @Test
    public void dadoElMetodoEliminarComentarioSeDebeRealizarUnaVez(){
        Comentario comentario = new Comentario();

        servicioComentario.eliminarComentario(comentario);

        verify(repositorioComentarioMock,times(1)).eliminarComentario(comentario);
    }
}
