package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Comentario;
import com.tallerwebi.infraestructura.config.HibernateTestConfigRepositorio;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.transaction.Transactional;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfigRepositorio.class})
@Transactional
public class RepositorioComentarioImplTest {

    private RepositorioComentarioImpl repositorioComentario;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        repositorioComentario = new RepositorioComentarioImpl(sessionFactory);
    }

    @Rollback
    @Test
    public void dadoQueElComentarioSeGuarda_guardarDebePersistirComentario(){
        Comentario comentario = new Comentario();
        comentario.setTexto("Este es un comentario de prueba");
        repositorioComentario.guardar(comentario);
        String hql = "from Comentario where texto = :texto";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("texto", "Este es un comentario de prueba");
        Comentario comentarioGuardado = (Comentario) query.getSingleResult();
        assertThat(comentarioGuardado.getTexto(), equalTo(comentario.getTexto()));

    }
}
