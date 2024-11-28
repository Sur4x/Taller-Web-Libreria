package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Service
@Transactional
public class ServicioLogroImpl implements ServicioLogro {

    @Autowired
    private RepositorioLogro repositorioLogro;

    @Override
    public Logro generarLogroAleatorio(Usuario usuario) {
        Logro logro = new Logro();
        Random random = new Random();

        if (random.nextBoolean()) {
            logro.setDescripcion("Leer " + (random.nextInt(5) + 1) + " libros");
            logro.setTipo("LIBROS");
        } else {
            logro.setDescripcion("Unirse a " + (random.nextInt(3) + 1) + " clubes");
            logro.setTipo("CLUBES");
        }

        logro.setUsuario(usuario);
        logro.setCumplido(false);
        repositorioLogro.guardar(logro);

        return logro;
    }

    @Override
    public void marcarLogroCumplido(Logro logro) {
        logro.setCumplido(true);
        repositorioLogro.guardar(logro);
    }

    @Override
    public Logro buscarLogroActivo(Long usuarioId) {
        return repositorioLogro.buscarLogroActivo(usuarioId);
    }

    @Override
    public int contarLogrosCumplidosPorTipo(Long usuarioId, String tipo) {
        return repositorioLogro.contarLogrosCumplidosPorTipo(usuarioId, tipo).size();
    }
}
