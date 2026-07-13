package com.sanosysalvos.notificaciones.service;

import com.sanosysalvos.notificaciones.entity.Notificacion;
import com.sanosysalvos.notificaciones.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        notificacion = Notificacion.builder()
                .idNotificacion(1L)
                .idUsuarioDestino(1L)
                .titulo("Test")
                .mensaje("Mensaje Test")
                .tipo("ALERTA")
                .urlRedireccion("/test")
                .leida(false)
                .build();
    }

    @Test
    void guardarNotificacion_shouldSave() {
        when(repository.save(any(Notificacion.class))).thenReturn(notificacion);
        notificacionService.guardarNotificacion(1L, "Test", "Mensaje Test", "ALERTA", "/test");
        verify(repository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void obtenerPorUsuario_shouldReturnList() {
        when(repository.findByIdUsuarioDestinoOrderByFechaCreacionDesc(1L)).thenReturn(Collections.singletonList(notificacion));
        List<Notificacion> result = notificacionService.obtenerPorUsuario(1L);
        assertEquals(1, result.size());
    }

    @Test
    void obtenerNoLeidasPorUsuario_shouldReturnList() {
        when(repository.findByIdUsuarioDestinoAndLeidaFalseOrderByFechaCreacionDesc(1L)).thenReturn(Collections.singletonList(notificacion));
        List<Notificacion> result = notificacionService.obtenerNoLeidasPorUsuario(1L);
        assertEquals(1, result.size());
    }

    @Test
    void marcarComoLeida_shouldUpdateAndReturn() {
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(repository.save(any(Notificacion.class))).thenReturn(notificacion);

        Notificacion result = notificacionService.marcarComoLeida(1L);

        assertTrue(result.getLeida());
        verify(repository, times(1)).save(notificacion);
    }
}
