package com.coffepotato.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.coffepotato.db.dao",
    transactionManagerRef = "mysql_transactionManager",
    entityManagerFactoryRef = "mySql_entityManagerFactory"
)
public class DbJWTConfig {


    @Autowired
    private org.springframework.core.env.Environment env;



    private static final String prefix = "spring.dbtest.datasource.hikari.";
    
    @Bean(name ="dbJWTSource")
    public HikariDataSource setDbAdminSource(){
        HikariConfig config = new HikariConfig();

        config.setPoolName("hickariCP-Db-JWTTest");
        config.setUsername(env.getProperty(prefix+"username"));
        config.setPassword(env.getProperty(prefix+"password"));
        config.setJdbcUrl(env.getProperty(prefix+"jdbc-url"));
        config.setMaxLifetime( Long.parseLong(env.getProperty(prefix+"max-lifetime")) );
        config.setConnectionTimeout(Long.parseLong( env.getProperty(prefix+"connection-timeout")));
        config.setValidationTimeout(Long.parseLong( env.getProperty(prefix+"validation-timeout")));

        config.addDataSourceProperty( "cachePrepStmts" ,  env.getProperty(prefix+"data-source-properties.cachePrepStmts"));
        config.addDataSourceProperty( "prepStmtCacheSize" , env.getProperty(prefix+"data-source-properties.prepStmtCacheSize"));
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , env.getProperty(prefix+"data-source-properties.prepStmtCacheSqlLimit") );
        config.addDataSourceProperty( "useServerPrepStmts" , env.getProperty(prefix+"data-source-properties.useServerPrepStmts") );

        HikariDataSource dataSource = new HikariDataSource(config);
        
        return dataSource;
    }


    @Primary
    @Bean(name = "mySql_entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dbJWTSource") DataSource dataSource) {

        Map<String,String> settings = new HashMap<>();
        //settings.put(Environment.HBM2DDL_AUTO, "update");
        settings.put(Environment.HBM2DDL_AUTO, "validate");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        settings.put(Environment.SHOW_SQL,"false");

        return builder.dataSource(dataSource)
                .packages("com.coffepotato.db.entity")
                .properties(settings)
                .build();
    }

    @Primary
    @Bean(name = "mysql_transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("mySql_entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }

}
