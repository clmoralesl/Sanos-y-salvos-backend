package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.dto.NotificacionEventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQProducer rabbitMQProducer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rabbitMQProducer, "exchange", "test_exchange");
        ReflectionTestUtils.setField(rabbitMQProducer, "routingKey", "test_routing_key");
    }

    @Test
    void enviarNotificacion_shouldSendMessage() {
        rabbitMQProducer.enviarNotificacion(1L, "Titulo", "Mensaje", "TIPO", "/url");

        verify(rabbitTemplate).convertAndSend(eq("test_exchange"), eq("test_routing_key"), any(NotificacionEventDTO.class));
    }
}
