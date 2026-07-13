package com.sanosysalvos.coincidencias.controller;

import com.sanosysalvos.coincidencias.domain.entity.Coincidencia;
import com.sanosysalvos.coincidencias.domain.enums.EstadoCoincidencia;
import com.sanosysalvos.coincidencias.service.CoincidenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CoincidenciaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CoincidenciaService coincidenciaService;

    @InjectMocks
    private CoincidenciaController coincidenciaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(coincidenciaController).build();
    }

    @Test
    void triggerCalculoCoincidencias_shouldReturnAccepted() throws Exception {
        mockMvc.perform(post("/coincidencias/v1/coincidencias/calcular/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("Cálculo de coincidencias iniciado para el reporte 100"));
    }

    @Test
    void getCoincidenciasPorReporte_shouldReturnList() throws Exception {
        Coincidencia c = new Coincidencia();
        c.setIdCoincidencia(1L);
        c.setReportePerdidaId(100L);
        c.setSimilitud(95.5);

        when(coincidenciaService.obtenerCoincidenciasPorReporte(100L)).thenReturn(List.of(c));

        mockMvc.perform(get("/coincidencias/v1/coincidencias/reporte/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCoincidencia").value(1))
                .andExpect(jsonPath("$[0].similitud").value(95.5));
    }

    @Test
    void actualizarEstadoCoincidencia_shouldReturnOk() throws Exception {
        Coincidencia c = new Coincidencia();
        c.setIdCoincidencia(1L);
        c.setEstado(EstadoCoincidencia.CONFIRMADA);

        when(coincidenciaService.actualizarEstadoCoincidencia(1L, "CONFIRMADA")).thenReturn(c);

        mockMvc.perform(put("/coincidencias/v1/coincidencias/1/estado")
                .param("nuevoEstado", "CONFIRMADA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCoincidencia").value(1))
                .andExpect(jsonPath("$.estado").value("CONFIRMADA"));
    }
}
