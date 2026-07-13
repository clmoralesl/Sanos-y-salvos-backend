package com.sanosysalvos.notificaciones.service;

import com.sanosysalvos.notificaciones.entity.Notificacion;
import com.sanosysalvos.notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository repository;

    public void guardarNotificacion(Long idUsuarioDestino, String titulo, String mensaje, String tipo, String urlRedireccion) {
        Notificacion notificacion = Notificacion.builder()
                .idUsuarioDestino(idUsuarioDestino)
                .titulo(titulo)
                .mensaje(mensaje)
                .tipo(tipo)
                .urlRedireccion(urlRedireccion)
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .build();
        repository.save(notificacion);
    }

    public List<Notificacion> obtenerPorUsuario(Long idUsuarioDestino) {
        return repository.findByIdUsuarioDestinoOrderByFechaCreacionDesc(idUsuarioDestino);
    }

    public List<Notificacion> obtenerNoLeidasPorUsuario(Long idUsuarioDestino) {
        return repository.findByIdUsuarioDestinoAndLeidaFalseOrderByFechaCreacionDesc(idUsuarioDestino);
    }

    public Notificacion marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = repository.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notificacion.setLeida(true);
        return repository.save(notificacion);
    }
}
