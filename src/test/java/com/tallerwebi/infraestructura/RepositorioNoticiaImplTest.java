package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Noticia;
import com.tallerwebi.infraestructura.config.HibernateTestConfigRepositorio;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfigRepositorio.class})
@Transactional
public class RepositorioNoticiaImplTest {

    private RepositorioNoticiaImpl repositorioNoticia;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        repositorioNoticia = new RepositorioNoticiaImpl(sessionFactory);
    }

    @Test
    @Rollback
    @Transactional
    public void dadoElMetodoObtenerNoticiasRandomCuandoExisten3NoticiasYSoloNecesita1Retorna2(){
        Integer cantidad = 2;
        Noticia noticia1 = new Noticia();
        Noticia noticia2 = new Noticia();
        Noticia noticia3 = new Noticia();

        repositorioNoticia.guardarNoticia(noticia1);
        repositorioNoticia.guardarNoticia(noticia2);
        repositorioNoticia.guardarNoticia(noticia3);

        String hql = "FROM Noticia ORDER BY function('RAND')";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setMaxResults(cantidad);
        List<Noticia> noticias =  query.getResultList();

        assertThat(noticias.size(), equalTo(2));
    }


}
