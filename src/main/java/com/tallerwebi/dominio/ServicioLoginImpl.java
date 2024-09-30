package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email) {
        return repositorioUsuario.buscarUsuario(email);
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(usuario.getEmail());
        if(usuarioEncontrado == null && usuario.getPassword().equals(usuario.getConfirmPassword())){
            usuario.setRol("usuario");
            usuario.setDescripcion("");
            repositorioUsuario.guardar(usuario);
        }else{
            throw new UsuarioExistente();
        }

    }

}


