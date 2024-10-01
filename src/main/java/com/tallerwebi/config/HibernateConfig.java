package com.tallerwebi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //dataSource.setDriverClassName("org.hsqldb.jdbcDriver"); //BDD en memoria
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); //BDD propia
        //dataSource.setUrl("jdbc:hsqldb:mem:db_");//BDD en memoria
        dataSource.setUrl("jdbc:mysql://localhost:3306/deUltima?createDatabaseIfNotExist=true"); //BDD propia URL + nombre
        dataSource.setUsername("root"); //cambie el usuario
        dataSource.setPassword(""); //cambie la contrasenia
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.tallerwebi.dominio"); //Aca busca las entidades para crear las tablas
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory(dataSource()).getObject());
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        //properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");//cambie el dialecto
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL8Dialect");//dialecto propio de la BDD
        properties.setProperty("hibernate.show_sql", "true");//muestra en la consola las sentencias de la tablas
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); //create = cada jetty run crea una nueva BDD.
        //update = mantiene los datos
        return properties;
    }
}
