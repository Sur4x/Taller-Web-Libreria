package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioNoticia {
    List<Noticia> obtenerNoticiasRandom(Integer cantidad);

    public void guardarNoticia(Noticia noticia);
}
