package com.project.backend.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;


@EnableTransactionManagement
@Configuration
public class R2dbcConfig extends AbstractR2dbcConfiguration {
    
    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        String url = "r2dbcs:mysql://scott:tiger@localhost:3306/bidding";        
        ConnectionFactory connectionFactory = ConnectionFactories.get(url);
        return connectionFactory;
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}