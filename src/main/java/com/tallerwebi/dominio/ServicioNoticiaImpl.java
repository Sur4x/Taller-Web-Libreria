package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioNoticiaImpl implements ServicioNoticia{

    private RepositorioNoticia repositorioNoticia;

    @Autowired
    public ServicioNoticiaImpl(RepositorioNoticia repositorioNoticia){
        this.repositorioNoticia = repositorioNoticia;

    }

    @Override
    public List<Noticia> obtenerNoticiasRandom(Integer cantidad){
        return repositorioNoticia.obtenerNoticiasRandom(cantidad);
    }
}
