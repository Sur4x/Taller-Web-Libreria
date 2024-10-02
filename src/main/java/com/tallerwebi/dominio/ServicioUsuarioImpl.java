package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenUsuarios;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario{

    @Autowired
    private RepositorioClub repositorioClub;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public List<Usuario> mostrarTodosLosUsuarios() throws NoExistenUsuarios {
        List<Usuario> usuarios = repositorioUsuario.buscarTodosLosUsuarios();
        if (!usuarios.isEmpty()){
            return usuarios;
        }else{
            throw new NoExistenUsuarios("No existen usuarios para mostrar");
        }
    }

    @Override
    public Usuario buscarUsuarioPor(Long id) throws NoExisteEseUsuario {
        Usuario usuario = repositorioUsuario.buscarUsuarioPor(id);
        if (usuario != null){
            return usuario;
        }else{
            throw new NoExisteEseUsuario("No existe un usuario con ese id");
        }

    }

    @Override
    public void guardarUsuario(Usuario usuario) throws NoExisteEseUsuario {
        repositorioUsuario.guardar(usuario);
    }

}
