package com.sanosysalvos.mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.mascotas.dto.MascotaRequestDTO;
import com.sanosysalvos.mascotas.dto.MascotaResponseDTO;
import com.sanosysalvos.mascotas.service.MascotaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MascotaController.class)
public class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MascotaService mascotaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetMascotaByIdSuccess() throws Exception {
        MascotaResponseDTO response = MascotaResponseDTO.builder()
                .idMascota(1L)
                .nombreMascota("Firulais")
                .nombreRaza("Pastor Aleman")
                .build();

        when(mascotaService.getMascotaById(1L)).thenReturn(response);

        mockMvc.perform(get("/mascotas/v1/mascotas/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMascota").value(1L))
                .andExpect(jsonPath("$.nombreMascota").value("Firulais"))
                .andExpect(jsonPath("$.nombreRaza").value("Pastor Aleman"));
    }

    @Test
    public void testCreateMascotaValidationError() throws Exception {
        MascotaRequestDTO invalidRequest = MascotaRequestDTO.builder()
                .nombreMascota("")
                .idRaza(null)
                .idTamanio(null)
                .build();

        mockMvc.perform(post("/mascotas/v1/mascotas")
                        .header("X-Auth0-Id", "auth0|123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateMascotaSuccess() throws Exception {
        MascotaRequestDTO request = MascotaRequestDTO.builder()
                .nombreMascota("Firulais")
                .idRaza(1L)
                .idTamanio(2L)
                .build();

        MascotaResponseDTO response = MascotaResponseDTO.builder()
                .idMascota(100L)
                .nombreMascota("Firulais")
                .build();

        when(mascotaService.createMascota(any(MascotaRequestDTO.class), eq("auth0|123"))).thenReturn(response);

        mockMvc.perform(post("/mascotas/v1/mascotas")
                        .header("X-Auth0-Id", "auth0|123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Mascota guardada correctamente"))
                .andExpect(jsonPath("$.data.idMascota").value(100L))
                .andExpect(jsonPath("$.data.nombreMascota").value("Firulais"));
    }
}
