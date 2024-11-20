package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteEseUsuario;
import com.tallerwebi.dominio.excepcion.NoExistenUsuarios;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
    public Usuario buscarUsuarioPor(Long id) throws NoExisteEseUsuario {
        Usuario usuario = repositorioUsuario.buscarUsuarioPor(id);
        if (usuario != null){
            Hibernate.initialize(usuario.getPuntuaciones());
            Hibernate.initialize(usuario.getSeguidores());
            Hibernate.initialize(usuario.getSeguidos());
            return usuario;
        }else{
            throw new NoExisteEseUsuario("No existe un usuario con ese id");
        }

    }

    @Override
    public boolean esAdmin(Usuario usuario) {
        return usuario.getRol().equals("admin");
    }

    @Override
    public void seguirUsuario(Usuario usuarioASeguir, Usuario usuarioActual) {
        usuarioASeguir.getSeguidores().add(usuarioActual);
        usuarioActual.getSeguidos().add(usuarioASeguir);
        repositorioUsuario.guardar(usuarioASeguir);
        repositorioUsuario.guardar(usuarioActual);
    }

    @Override
    public void dejarDeSeguirUsuario(Usuario usuarioASeguir, Usuario usuarioActual) {
        usuarioASeguir.getSeguidores().remove(usuarioActual);
        usuarioActual.getSeguidos().remove(usuarioASeguir);
        repositorioUsuario.guardar(usuarioASeguir);
        repositorioUsuario.guardar(usuarioActual);
    }

    @Override
    public List<Usuario> obtenerUsuariosConMasSeguidores() {
        List<Usuario> usuarios = repositorioUsuario.buscarTodosLosUsuarios();

        for (Usuario usuario : usuarios) {
            Hibernate.initialize(usuario.getSeguidores());
        }

        usuarios.sort(Comparator.comparingInt(s -> s.getSeguidores().size()));
        Collections.reverse(usuarios);
        List<Usuario> usuariosConMasSeguidores = new ArrayList<>();

        for(int i=0;i<5 && i < usuarios.size();i++){
            usuariosConMasSeguidores.add(usuarios.get(i));
        }

        return usuariosConMasSeguidores;
    }

    @Override
    public Set<Usuario> obtenerUsuariosSeguidos(Usuario usuario){
        Hibernate.initialize(usuario.getSeguidos());
        return usuario.getSeguidos();
    }
}
