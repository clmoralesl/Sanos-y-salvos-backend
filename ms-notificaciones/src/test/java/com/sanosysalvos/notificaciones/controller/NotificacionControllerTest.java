package com.sanosysalvos.notificaciones.controller;

import com.sanosysalvos.notificaciones.entity.Notificacion;
import com.sanosysalvos.notificaciones.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        notificacion = Notificacion.builder()
                .idNotificacion(1L)
                .idUsuarioDestino(2L)
                .titulo("Test")
                .mensaje("Mensaje Test")
                .leida(false)
                .build();
    }

    @Test
    void obtenerPorUsuario_shouldReturnList() throws Exception {
        when(notificacionService.obtenerPorUsuario(2L)).thenReturn(Collections.singletonList(notificacion));

        mockMvc.perform(get("/notificaciones/v1/notificaciones/usuario/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idNotificacion").value(1L))
                .andExpect(jsonPath("$[0].titulo").value("Test"));
    }

    @Test
    void obtenerPorUsuario_soloNoLeidas_shouldReturnList() throws Exception {
        when(notificacionService.obtenerNoLeidasPorUsuario(2L)).thenReturn(Collections.singletonList(notificacion));

        mockMvc.perform(get("/notificaciones/v1/notificaciones/usuario/2?soloNoLeidas=true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idNotificacion").value(1L));
    }

    @Test
    void marcarComoLeida_shouldReturnUpdated() throws Exception {
        Notificacion leida = Notificacion.builder().idNotificacion(1L).leida(true).build();
        when(notificacionService.marcarComoLeida(1L)).thenReturn(leida);

        mockMvc.perform(put("/notificaciones/v1/notificaciones/1/leer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leida").value(true));
    }
}
