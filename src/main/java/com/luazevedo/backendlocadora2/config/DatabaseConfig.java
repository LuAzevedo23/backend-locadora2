package com.luazevedo.backendlocadora2.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://org-rob-dev-inst-instance-3.data-1.use1.tembo.io:5432/robson_locadora");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("Tf888olz3PHfCIC2");

        return dataSourceBuilder.build();

    }

}
