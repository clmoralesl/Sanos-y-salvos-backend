package com.sanosysalvos.notificaciones.listener;

import com.sanosysalvos.notificaciones.dto.NotificacionEventDTO;
import com.sanosysalvos.notificaciones.dto.UsuarioDTO;
import com.sanosysalvos.notificaciones.integration.MascotasClient;
import com.sanosysalvos.notificaciones.service.EmailService;
import com.sanosysalvos.notificaciones.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionListenerTest {

    @Mock
    private NotificacionService notificacionService;

    @Mock
    private MascotasClient mascotasClient;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificacionListener listener;

    private NotificacionEventDTO eventoCoincidencia;
    private NotificacionEventDTO eventoNormal;

    @BeforeEach
    void setUp() {
        eventoCoincidencia = new NotificacionEventDTO();
        eventoCoincidencia.setIdUsuarioDestino(1L);
        eventoCoincidencia.setTitulo("Nueva coincidencia encontrada!");
        eventoCoincidencia.setMensaje("Hemos encontrado una posible coincidencia.");
        eventoCoincidencia.setTipo("ALERTA");

        eventoNormal = new NotificacionEventDTO();
        eventoNormal.setIdUsuarioDestino(2L);
        eventoNormal.setTitulo("Bienvenido");
        eventoNormal.setMensaje("Bienvenido a la app");
        eventoNormal.setTipo("INFO");
    }

    @Test
    void recibirNotificacion_withCoincidencia_shouldSaveAndSendEmail() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        // usuarioDTO.setId(1L); // No existe en DTO
        usuarioDTO.setEmail("test@test.com");

        when(mascotasClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioDTO);

        listener.recibirNotificacion(eventoCoincidencia);

        verify(notificacionService, times(1)).guardarNotificacion(1L, "Nueva coincidencia encontrada!", "Hemos encontrado una posible coincidencia.", "ALERTA", null);
        verify(emailService, times(1)).enviarCorreo("test@test.com", "Nueva coincidencia encontrada!", "Hemos encontrado una posible coincidencia.");
    }

    @Test
    void recibirNotificacion_withNormal_shouldSaveOnly() {
        listener.recibirNotificacion(eventoNormal);

        verify(notificacionService, times(1)).guardarNotificacion(2L, "Bienvenido", "Bienvenido a la app", "INFO", null);
        verify(emailService, never()).enviarCorreo(anyString(), anyString(), anyString());
    }

    @Test
    void recibirNotificacion_withException_shouldHandleGracefully() {
        doThrow(new RuntimeException("Error simulado")).when(notificacionService).guardarNotificacion(anyLong(), anyString(), anyString(), anyString(), any());
        
        listener.recibirNotificacion(eventoNormal);
        
        verify(notificacionService, times(1)).guardarNotificacion(2L, "Bienvenido", "Bienvenido a la app", "INFO", null);
        verify(emailService, never()).enviarCorreo(anyString(), anyString(), anyString());
    }
}
