package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenUsuarios;

import java.util.List;

public interface ServicioUsuario {

    Usuario buscarUsuarioPor(Long id) throws NoExisteEseUsuario;



}
