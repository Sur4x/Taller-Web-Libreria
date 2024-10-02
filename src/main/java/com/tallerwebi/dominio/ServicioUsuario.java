package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenUsuarios;

import java.util.List;

public interface ServicioUsuario {

    public List<Usuario> mostrarTodosLosUsuarios() throws NoExistenUsuarios;

    Usuario buscarUsuarioPor(Long id) throws NoExisteEseUsuario;

    public void guardarUsuario(Usuario usuario) throws NoExisteEseUsuario;

}
