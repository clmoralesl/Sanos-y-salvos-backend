package com.sanosysalvos.notificaciones.service.impl;

import com.sanosysalvos.notificaciones.entity.Notificacion;
import com.sanosysalvos.notificaciones.repository.NotificacionRepository;
import com.sanosysalvos.notificaciones.service.NotificacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceImplTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    void guardarNotificacion_DeberiaGuardarCorrectamente() {
        Long idUsuario = 1L;
        String titulo = "Titulo";
        String mensaje = "Mensaje";
        String tipo = "ALERTA";
        String url = "url";

        notificacionService.guardarNotificacion(idUsuario, titulo, mensaje, tipo, url);

        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionRepository).save(captor.capture());

        Notificacion guardada = captor.getValue();
        assertEquals(idUsuario, guardada.getIdUsuarioDestino());
        assertEquals(titulo, guardada.getTitulo());
        assertEquals(mensaje, guardada.getMensaje());
        assertEquals(tipo, guardada.getTipo());
        assertEquals(url, guardada.getUrlRedireccion());
        assertFalse(guardada.getLeida());
        assertNotNull(guardada.getFechaCreacion());
    }

    @Test
    void obtenerPorUsuario_DeberiaRetornarLista() {
        Long idUsuario = 1L;
        List<Notificacion> mockLista = List.of(new Notificacion());
        when(notificacionRepository.findByIdUsuarioDestinoOrderByFechaCreacionDesc(idUsuario))
                .thenReturn(mockLista);

        List<Notificacion> resultado = notificacionService.obtenerPorUsuario(idUsuario);

        assertEquals(mockLista, resultado);
        verify(notificacionRepository).findByIdUsuarioDestinoOrderByFechaCreacionDesc(idUsuario);
    }

    @Test
    void obtenerNoLeidasPorUsuario_DeberiaRetornarLista() {
        Long idUsuario = 1L;
        List<Notificacion> mockLista = List.of(new Notificacion());
        when(notificacionRepository.findByIdUsuarioDestinoAndLeidaFalseOrderByFechaCreacionDesc(idUsuario))
                .thenReturn(mockLista);

        List<Notificacion> resultado = notificacionService.obtenerNoLeidasPorUsuario(idUsuario);

        assertEquals(mockLista, resultado);
        verify(notificacionRepository).findByIdUsuarioDestinoAndLeidaFalseOrderByFechaCreacionDesc(idUsuario);
    }

    @Test
    void marcarComoLeida_DeberiaActualizarYGuardar() {
        Long idNotificacion = 1L;
        Notificacion notificacion = Notificacion.builder()
                .idNotificacion(idNotificacion)
                .leida(false)
                .build();

        when(notificacionRepository.findById(idNotificacion)).thenReturn(Optional.of(notificacion));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        Notificacion resultado = notificacionService.marcarComoLeida(idNotificacion);

        assertTrue(resultado.getLeida());
        verify(notificacionRepository).findById(idNotificacion);
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    void marcarComoLeida_DeberiaLanzarExcepcion_CuandoNoExiste() {
        Long idNotificacion = 1L;
        when(notificacionRepository.findById(idNotificacion)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                notificacionService.marcarComoLeida(idNotificacion));

        assertEquals("Notificación no encontrada", exception.getMessage());
        verify(notificacionRepository).findById(idNotificacion);
        verify(notificacionRepository, never()).save(any());
    }
}
