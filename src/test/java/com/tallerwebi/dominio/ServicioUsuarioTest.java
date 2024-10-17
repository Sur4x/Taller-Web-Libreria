package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenUsuarios;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioUsuarioTest {
    @Mock
    private RepositorioUsuario repositorioUsuario;
    @InjectMocks
    private ServicioUsuarioImpl servicioUsuario;
    private Usuario usuario;
    @BeforeEach
    void init(){
        usuario = mock(Usuario.class);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void dadoElMetodoBuscarUsuarioPorIdNoEncuentreAlUsuarioEnElSistemaLanzaUnaExcepcion() {

        Mockito.when(repositorioUsuario.buscarUsuarioPor(anyLong())).thenReturn(null);

        NoExisteEseUsuario exception = assertThrows(NoExisteEseUsuario.class, () -> servicioUsuario.buscarUsuarioPor(1L));

        assertEquals(exception.getMessage(), "No existe un usuario con ese id");
    }

    @Test
    void dadoElMetodoBuscarUsuarioPorIdCuandoEncuentreAlUsuarioEnElSistemaLoDevuelve() throws NoExisteEseUsuario {

        Mockito.when(repositorioUsuario.buscarUsuarioPor(anyLong())).thenReturn(usuario);

        Usuario resultado = servicioUsuario.buscarUsuarioPor(1L);

        assertThat(resultado.getId(), Matchers.is(usuario.getId()));
    }

    @Test
    void dadoElMetodoEsAdminSiElUsuarioEsAdminDevuelveTrue() throws NoExisteEseUsuario {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setRol("admin");
        Boolean esAdmin = servicioUsuario.esAdmin(usuarioTest);

        assertThat(esAdmin, equalTo(true));
    }

    @Test
    void dadoElMetodoEsAdminSiElUsuarioEsNormalDevuelveFalse() throws NoExisteEseUsuario {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setRol("usuario");
        Boolean esAdmin = servicioUsuario.esAdmin(usuarioTest);

        assertThat(esAdmin, equalTo(false));
    }

}
