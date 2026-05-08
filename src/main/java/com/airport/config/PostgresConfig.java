package com.airport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.airport.postgres.repository")
@EnableTransactionManagement
public class PostgresConfig {
    // O Spring Boot autoconfigura o DataSource e o EntityManager
    // automaticamente a partir do application.yml (spring.datasource.*)
    // Essa classe ativa os repositories JPA e o suporte a @Transactional
}
