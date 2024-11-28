package com.tallerwebi.dominio;

public interface ServicioLogro {
    Logro generarLogroAleatorio(Usuario usuario);

    void marcarLogroCumplido(Logro logro);

    Logro buscarLogroActivo(Long usuarioId);

    int contarLogrosCumplidosPorTipo(Long usuarioId, String tipo);
}
