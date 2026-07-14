package com.sanosysalvos.coincidencias.messaging;

import com.sanosysalvos.coincidencias.service.CoincidenciaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RabbitMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final CoincidenciaService coincidenciaService;

    public RabbitMQConsumer(CoincidenciaService coincidenciaService) {
        this.coincidenciaService = coincidenciaService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.reportes}")
    public void consumeReporteEvent(Long reporteId) {
        log.info("Mensaje recibido para procesar reporte ID: {}", reporteId);
        coincidenciaService.procesarReporte(reporteId);
    }
}
