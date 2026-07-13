package com.sanosysalvos.coincidencias.service.impl;

import com.sanosysalvos.coincidencias.integration.dto.NotificacionEventDTO;
import com.sanosysalvos.coincidencias.service.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducerImpl implements RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${notificaciones.exchange.name:notificaciones_exchange}")
    private String exchange;

    @Value("${notificaciones.routing.key:notificaciones_routing_key}")
    private String routingKey;

    @Override
    public void enviarNotificacion(Long idUsuarioDestino, String titulo, String mensaje, String tipo, String urlRedireccion) {
        if (idUsuarioDestino == null) {
            log.warn("No se puede enviar notificación: el idUsuarioDestino es nulo");
            return;
        }

        NotificacionEventDTO evento = NotificacionEventDTO.builder()
                .idUsuarioDestino(idUsuarioDestino)
                .titulo(titulo)
                .mensaje(mensaje)
                .tipo(tipo)
                .urlRedireccion(urlRedireccion)
                .build();
        
        log.info("Enviando notificación a RabbitMQ (desde ms-coincidencias): {}", evento);
        rabbitTemplate.convertAndSend(exchange, routingKey, evento);
    }
}
