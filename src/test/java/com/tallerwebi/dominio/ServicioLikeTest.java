package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ServicioLikeTest {

    private ServicioLike servicioLike;
    private RepositorioLike repositorioLikeMock;
    private RepositorioComentario repositorioComentarioMock;

    @BeforeEach
    public void init(){
        repositorioLikeMock = mock(RepositorioLike.class);
        repositorioComentarioMock = mock(RepositorioComentario.class);
        this.servicioLike = new ServicioLikeImpl(repositorioLikeMock,repositorioComentarioMock);
    }
    //el metodo validarSiUnUsuarioDioLikeAUnComentario se usa 1 vez al menos, si torona false, verificar que buscarComentarioPorId y agregarLike se usen 1 vez
    @Test
    public void dadoUnServicioLikeSiElUsuarioNoDioLikePreviamenteAgregaElLike(){
        Long comentarioId = 1L;
        Usuario usuario = new Usuario();

        when(repositorioLikeMock.validarSiUnUsuarioDioLikeAUnComentario(comentarioId, usuario.getId())).thenReturn(false);
        when(repositorioComentarioMock.buscarComentarioPorId(comentarioId)).thenReturn(new Comentario());

        Boolean agregado = servicioLike.agregarLike(comentarioId, usuario);

        verify(repositorioLikeMock, times(1)).validarSiUnUsuarioDioLikeAUnComentario(comentarioId, usuario.getId());
        verify(repositorioComentarioMock,times(1)).buscarComentarioPorId(comentarioId);
        verify(repositorioLikeMock, times(1)).agregarLike(any(Like.class)); // acepta cualquier objeto pero de la clase Like especificamente
        assertThat(agregado, equalTo(true));
    }

    //el metodo validarSiUnUsuarioDioLikeAUnComentario se usa 1 vez al menos, si torona true, verificar que buscarComentarioPorId y agregarLike NO SE USEN
    @Test
    public void dadoUnServicioLikeSiElUsuarioDioLikePreviamenteNoSePuedeAgregarEseLike(){
        Long comentarioId = 1L;
        Usuario usuario = new Usuario();

        when(repositorioLikeMock.validarSiUnUsuarioDioLikeAUnComentario(comentarioId, usuario.getId())).thenReturn(true);

        Boolean agregado = servicioLike.agregarLike(comentarioId, usuario);

        verify(repositorioLikeMock, times(1)).validarSiUnUsuarioDioLikeAUnComentario(comentarioId, usuario.getId());
        verify(repositorioComentarioMock,times(0)).buscarComentarioPorId(comentarioId);
        verify(repositorioLikeMock, times(0)).agregarLike(any(Like.class)); // acepta cualquier objeto pero de la clase Like especificamente
        assertThat(agregado, equalTo(false));
    }
}
