package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioNoticia {

    List<Noticia> obtenerNoticiasRandom(Integer cantidad);
}
