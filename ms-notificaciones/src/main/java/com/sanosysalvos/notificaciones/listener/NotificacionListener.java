package com.sanosysalvos.notificaciones.listener;

import com.sanosysalvos.notificaciones.dto.NotificacionEventDTO;
import com.sanosysalvos.notificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificacionListener {

    private final NotificacionService notificacionService;

    @RabbitListener(queues = "${notificaciones.queue.name}")
    public void recibirNotificacion(NotificacionEventDTO evento) {
        log.info("Mensaje recibido de RabbitMQ: {}", evento);
        try {
            notificacionService.guardarNotificacion(
                    evento.getIdUsuarioDestino(),
                    evento.getTitulo(),
                    evento.getMensaje(),
                    evento.getTipo()
            );
            log.info("Notificación persistida exitosamente para el usuario {}", evento.getIdUsuarioDestino());
        } catch (Exception e) {
            log.error("Error al procesar la notificación: {}", e.getMessage());
        }
    }
}
