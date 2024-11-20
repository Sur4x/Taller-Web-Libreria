package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenUsuarios;

import java.util.List;
import java.util.Set;

public interface ServicioUsuario {

    Usuario buscarUsuarioPor(Long id) throws NoExisteEseUsuario;

    boolean esAdmin(Usuario usuario);

    void seguirUsuario(Usuario usuarioASeguir, Usuario usuarioActual);

    void dejarDeSeguirUsuario(Usuario usuarioASeguir, Usuario usuarioActual);

    List<Usuario> obtenerUsuariosConMasSeguidores();

    Set<Usuario> obtenerUsuariosSeguidos(Usuario usuario);
}
