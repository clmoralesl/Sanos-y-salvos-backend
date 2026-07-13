package com.sanosysalvos.coincidencias.service;

public interface RabbitMQProducer {
    void enviarNotificacion(Long idUsuarioDestino, String titulo, String mensaje, String tipo, String urlRedireccion);
}
