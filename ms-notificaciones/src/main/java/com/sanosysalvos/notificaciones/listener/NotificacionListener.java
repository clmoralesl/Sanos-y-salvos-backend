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
    private final com.sanosysalvos.notificaciones.integration.MascotasClient mascotasClient;
    private final com.sanosysalvos.notificaciones.service.EmailService emailService;

    @RabbitListener(queues = "${notificaciones.queue.name}")
    public void recibirNotificacion(NotificacionEventDTO evento) {
        log.info("Mensaje recibido de RabbitMQ: {}", evento);
        try {
            notificacionService.guardarNotificacion(
                    evento.getIdUsuarioDestino(),
                    evento.getTitulo(),
                    evento.getMensaje(),
                    evento.getTipo(),
                    evento.getUrlRedireccion()
            );
            log.info("Notificación persistida exitosamente para el usuario {}", evento.getIdUsuarioDestino());

            // Si es una notificación de coincidencia, enviar correo
            if (evento.getTitulo() != null && evento.getTitulo().toLowerCase().contains("coincidencia")) {
                try {
                    com.sanosysalvos.notificaciones.dto.UsuarioDTO usuario = mascotasClient.obtenerUsuarioPorId(evento.getIdUsuarioDestino());
                    if (usuario != null && usuario.getEmail() != null) {
                        emailService.enviarCorreo(usuario.getEmail(), evento.getTitulo(), evento.getMensaje());
                    }
                } catch (Exception e) {
                    log.error("Error al obtener email del usuario {} para notificación: {}", evento.getIdUsuarioDestino(), e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Error al procesar la notificación: {}", e.getMessage());
        }
    }
}
