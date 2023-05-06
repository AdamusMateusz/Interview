package com.cdq.interview.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration{

    @Value("${cdq.database.name}")
    private String database;

    @Value("${cdq.database.host}")
    private String host;

    @Value("${cdq.database.port:5432}")
    private int port;

    @Value("${cdq.database.username}")
    private String username;

    @Value("${cdq.database.password}")
    private String password;

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        Map<String, String> options = new HashMap<>();

        options.put("lock_timeout", "10s");
        options.put("statement_timeout", "5m");

        return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .username(username)
                .password(password)
                .database(database)
                .options(options)
                .build());
    }

}
