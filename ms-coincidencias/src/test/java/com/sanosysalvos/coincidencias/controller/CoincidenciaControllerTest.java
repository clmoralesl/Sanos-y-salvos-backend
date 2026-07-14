package com.sanosysalvos.coincidencias.controller;

import com.sanosysalvos.coincidencias.domain.entity.Coincidencia;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CoincidenciaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CoincidenciaService coincidenciaService;

    @Mock
    private com.sanosysalvos.coincidencias.repository.CoincidenciaRepository coincidenciaRepository;

    @InjectMocks
    private CoincidenciaController coincidenciaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(coincidenciaController).build();
    }

    @Test
    void procesarReporteTrigger_shouldReturnAccepted() throws Exception {
        mockMvc.perform(post("/coincidencias/v1/coincidencias/trigger/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("Procesamiento de coincidencias iniciado para el reporte 100"));
    }

    @Test
    void obtenerPorPerdida_shouldReturnList() throws Exception {
        Coincidencia c = new Coincidencia();
        c.setId(1L);
        c.setReportePerdidaId(100L);
        c.setPorcentajeSimilitud(95.5);

        when(coincidenciaRepository.findByReportePerdidaId(100L)).thenReturn(List.of(c));

        mockMvc.perform(get("/coincidencias/v1/coincidencias/perdida/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].porcentajeSimilitud").value(95.5));
    }

    @Test
    void obtenerPorHallazgo_shouldReturnList() throws Exception {
        Coincidencia c = new Coincidencia();
        c.setId(2L);
        c.setReporteHallazgoId(200L);
        c.setPorcentajeSimilitud(85.5);

        when(coincidenciaRepository.findByReporteHallazgoId(200L)).thenReturn(List.of(c));

        mockMvc.perform(get("/coincidencias/v1/coincidencias/hallazgo/200")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].porcentajeSimilitud").value(85.5));
    }
}
