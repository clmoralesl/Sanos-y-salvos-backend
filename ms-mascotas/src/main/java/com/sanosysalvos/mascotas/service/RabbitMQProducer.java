package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.dto.NotificacionEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${notificaciones.exchange.name:notificaciones_exchange}")
    private String exchange;

    @Value("${notificaciones.routing.key:notificaciones_routing_key}")
    private String routingKey;

    public void enviarNotificacion(Long idUsuarioDestino, String titulo, String mensaje, String tipo) {
        NotificacionEventDTO evento = NotificacionEventDTO.builder()
                .idUsuarioDestino(idUsuarioDestino)
                .titulo(titulo)
                .mensaje(mensaje)
                .tipo(tipo)
                .build();
        
        log.info("Enviando notificación a RabbitMQ: {}", evento);
        rabbitTemplate.convertAndSend(exchange, routingKey, evento);
    }
}
