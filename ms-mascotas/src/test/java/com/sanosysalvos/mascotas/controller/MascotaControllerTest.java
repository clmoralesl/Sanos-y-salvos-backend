package com.sanosysalvos.mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.mascotas.dto.MascotaRequestDTO;
import com.sanosysalvos.mascotas.dto.MascotaResponseDTO;
import com.sanosysalvos.mascotas.service.MascotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MascotaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MascotaService mascotaService;

    @InjectMocks
    private MascotaController mascotaController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mascotaController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createMascota_success() throws Exception {
        MascotaRequestDTO request = new MascotaRequestDTO();
        request.setNombreMascota("Fido");
        request.setIdRaza(1L);
        request.setIdTamanio(1L);
        MascotaResponseDTO response = new MascotaResponseDTO();
        
        when(mascotaService.createMascota(any(MascotaRequestDTO.class), eq("auth0-id")))
                .thenReturn(response);

        mockMvc.perform(post("/mascotas/v1/mascotas")
                .header("X-Auth0-Id", "auth0-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Mascota guardada correctamente"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void createMascota_missingHeader() throws Exception {
        MascotaRequestDTO request = new MascotaRequestDTO();

        mockMvc.perform(post("/mascotas/v1/mascotas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMascotaById_success() throws Exception {
        MascotaResponseDTO response = new MascotaResponseDTO();
        
        when(mascotaService.getMascotaById(1L)).thenReturn(response);

        mockMvc.perform(get("/mascotas/v1/mascotas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllMascotas_success() throws Exception {
        List<MascotaResponseDTO> responseList = Arrays.asList(new MascotaResponseDTO(), new MascotaResponseDTO());
        
        when(mascotaService.getAllMascotas()).thenReturn(responseList);

        mockMvc.perform(get("/mascotas/v1/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getMisMascotas_success() throws Exception {
        List<MascotaResponseDTO> responseList = Arrays.asList(new MascotaResponseDTO());
        
        when(mascotaService.getMisMascotas("auth0-id")).thenReturn(responseList);

        mockMvc.perform(get("/mascotas/v1/mascotas/me")
                .header("X-Auth0-Id", "auth0-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
    
    @Test
    void getMisMascotas_missingHeader() throws Exception {
        mockMvc.perform(get("/mascotas/v1/mascotas/me"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMascota_success() throws Exception {
        MascotaRequestDTO request = new MascotaRequestDTO();
        request.setNombreMascota("Fido");
        request.setIdRaza(1L);
        request.setIdTamanio(1L);
        MascotaResponseDTO response = new MascotaResponseDTO();
        
        when(mascotaService.updateMascota(eq(1L), any(MascotaRequestDTO.class), eq("auth0-id")))
                .thenReturn(response);

        mockMvc.perform(put("/mascotas/v1/mascotas/1")
                .header("X-Auth0-Id", "auth0-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Mascota actualizada correctamente"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void deleteMascota_success() throws Exception {
        doNothing().when(mascotaService).deleteMascota(1L);

        mockMvc.perform(delete("/mascotas/v1/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Mascota 1 eliminada correctamente"));
    }
}
