package com.gp.api;

import org.flywaydb.core.Flyway;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
public class PostgreTestConfig {
    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.8");
        postgreSQLContainer.start();
        return postgreSQLContainer;
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        DataSource build = DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
        Flyway.configure().dataSource(build).load().migrate();
        return build;
    }
}
