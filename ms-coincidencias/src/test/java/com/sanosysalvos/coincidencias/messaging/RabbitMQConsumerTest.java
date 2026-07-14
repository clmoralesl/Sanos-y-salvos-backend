package com.sanosysalvos.coincidencias.messaging;

import com.sanosysalvos.coincidencias.service.CoincidenciaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class RabbitMQConsumerTest {

    @Mock
    private CoincidenciaService coincidenciaService;

    @InjectMocks
    private RabbitMQConsumer rabbitMQConsumer;

    @Test
    void testConsumeReporteEvent() {
        Long reporteId = 123L;

        rabbitMQConsumer.consumeReporteEvent(reporteId);

        verify(coincidenciaService, times(1)).procesarReporte(reporteId);
    }
}
