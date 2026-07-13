package com.sanosysalvos.coincidencias.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${notificaciones.exchange.name:notificaciones_exchange}")
    private String exchangeName;

    @Bean
    public TopicExchange notificacionesExchange() {
        return new TopicExchange(exchangeName);
    }
}
