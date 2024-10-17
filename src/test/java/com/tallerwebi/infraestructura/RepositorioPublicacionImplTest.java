package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
@Transactional
public class RepositorioPublicacionImplTest {

    private RepositorioPublicacionImpl repositorioPublicacion;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        repositorioPublicacion = new RepositorioPublicacionImpl(sessionFactory);
    }

    @Test
    public void eliminarPublicacion() {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Reporte 1");
        publicacion.setMensaje("Este es un mssj de reporte");
        repositorioPublicacion.guardar(publicacion);
        repositorioPublicacion.eliminar(publicacion);
        Publicacion resultado = repositorioPublicacion.buscarPublicacionPorId(publicacion.getId());
        assertThat(resultado, is(nullValue()));
    }
}
