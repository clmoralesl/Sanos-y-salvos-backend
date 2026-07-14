package com.sanosysalvos.mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.mascotas.dto.FiltroBusquedaMasivaDTO;
import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.service.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ReporteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReporteService reporteService;

    @InjectMocks
    private ReporteController reporteController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reporteController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Test
    void crearReporteTest() throws Exception {
        ReporteRequestDTO requestDTO = new ReporteRequestDTO();
        requestDTO.setIdTipoReporte(1L);
        requestDTO.setIdMascota(1L);
        requestDTO.setIdUbicacionReporte(1L);
        requestDTO.setFechaIncidente(java.time.LocalDateTime.now());
        ReporteResponseDTO responseDTO = new ReporteResponseDTO();

        when(reporteService.crearReporte(any(ReporteRequestDTO.class), eq("auth0-id"))).thenReturn(responseDTO);

        mockMvc.perform(post("/mascotas/v1/reportes")
                .header("X-Auth0-Id", "auth0-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Reporte creado exitosamente"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void obtenerTodosTest() throws Exception {
        ReporteResponseDTO responseDTO = new ReporteResponseDTO();
        List<ReporteResponseDTO> list = Collections.singletonList(responseDTO);

        when(reporteService.obtenerTodosLosReportes()).thenReturn(list);

        mockMvc.perform(get("/mascotas/v1/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void obtenerPorIdTest() throws Exception {
        Long reporteId = 1L;
        ReporteResponseDTO responseDTO = new ReporteResponseDTO();

        when(reporteService.obtenerReportePorId(reporteId)).thenReturn(responseDTO);

        mockMvc.perform(get("/mascotas/v1/reportes/{id}", reporteId))
                .andExpect(status().isOk());
    }

    @Test
    void cerrarReporteTest() throws Exception {
        Long reporteId = 1L;
        ReporteResponseDTO responseDTO = new ReporteResponseDTO();

        when(reporteService.cerrarReporte(eq(reporteId), eq("auth0-id"))).thenReturn(responseDTO);

        mockMvc.perform(put("/mascotas/v1/reportes/{id}/cerrar", reporteId)
                .header("X-Auth0-Id", "auth0-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reporte " + reporteId + " cerrado correctamente"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void actualizarReporteTest() throws Exception {
        Long reporteId = 1L;
        ReporteRequestDTO requestDTO = new ReporteRequestDTO();
        requestDTO.setIdTipoReporte(1L);
        requestDTO.setIdMascota(1L);
        requestDTO.setIdUbicacionReporte(1L);
        requestDTO.setFechaIncidente(java.time.LocalDateTime.now());
        ReporteResponseDTO responseDTO = new ReporteResponseDTO();

        when(reporteService.actualizarReporte(eq(reporteId), any(ReporteRequestDTO.class), eq("auth0-id"))).thenReturn(responseDTO);

        mockMvc.perform(put("/mascotas/v1/reportes/{id}", reporteId)
                .header("X-Auth0-Id", "auth0-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reporte " + reporteId + " actualizado correctamente"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void eliminarReporteTest() throws Exception {
        Long reporteId = 1L;

        doNothing().when(reporteService).eliminarReporte(reporteId, "auth0-id");

        mockMvc.perform(delete("/mascotas/v1/reportes/{id}", reporteId)
                .header("X-Auth0-Id", "auth0-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reporte " + reporteId + " eliminado correctamente"));
    }

    @Test
    void buscarReportesCandidatosTest() throws Exception {
        FiltroBusquedaMasivaDTO filtro = new FiltroBusquedaMasivaDTO();
        ReporteResponseDTO responseDTO = new ReporteResponseDTO();
        List<ReporteResponseDTO> list = Collections.singletonList(responseDTO);

        when(reporteService.buscarReportesCandidatos(any(FiltroBusquedaMasivaDTO.class))).thenReturn(list);

        mockMvc.perform(post("/mascotas/v1/reportes/busqueda-masiva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filtro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void crearReporteSinAuthHeaderTest() throws Exception {
        ReporteRequestDTO requestDTO = new ReporteRequestDTO();

        mockMvc.perform(post("/mascotas/v1/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarReporteSinAuthHeaderTest() throws Exception {
        Long reporteId = 1L;
        ReporteRequestDTO requestDTO = new ReporteRequestDTO();

        mockMvc.perform(put("/mascotas/v1/reportes/{id}", reporteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}
