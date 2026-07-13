package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.dto.ReporteDetalleDTO;
import com.sanosysalvos.bff.service.OrchestrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BffReporteController.class)
class BffReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrchestrationService orchestrationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void obtenerDetalleCompleto_shouldReturnDetalle() throws Exception {
        ReporteDetalleDTO dto = ReporteDetalleDTO.builder()
                .idReporte(1L)
                .build();

        when(orchestrationService.obtenerDetalleCompleto(1L, "auth0|123")).thenReturn(dto);

        mockMvc.perform(get("/bff/v1/reportes/1/detalle")
                .header("X-Auth0-Id", "auth0|123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReporte").value(1L));
    }
}
