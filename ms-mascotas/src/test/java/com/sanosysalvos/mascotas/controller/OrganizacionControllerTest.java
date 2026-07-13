package com.sanosysalvos.mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.mascotas.dto.OrganizacionRequestDTO;
import com.sanosysalvos.mascotas.dto.OrganizacionResponseDTO;
import com.sanosysalvos.mascotas.service.OrganizacionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrganizacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrganizacionService organizacionService;

    @InjectMocks
    private OrganizacionController organizacionController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(organizacionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void crearOrganizacion_shouldReturnCreated() throws Exception {
        OrganizacionRequestDTO req = new OrganizacionRequestDTO();
        req.setNombreOrganizacion("Org Test");

        OrganizacionResponseDTO res = OrganizacionResponseDTO.builder()
                .idOrganizacion(1L)
                .nombreOrganizacion("Org Test")
                .build();

        when(organizacionService.crearOrganizacion(any(OrganizacionRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/mascotas/v1/organizaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idOrganizacion").value(1))
                .andExpect(jsonPath("$.nombreOrganizacion").value("Org Test"));
    }

    @Test
    void listarOrganizaciones_shouldReturnOk() throws Exception {
        when(organizacionService.obtenerTodas()).thenReturn(List.of(
                OrganizacionResponseDTO.builder().idOrganizacion(1L).build()
        ));

        mockMvc.perform(get("/mascotas/v1/organizaciones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOrganizacion").value(1));
    }

    @Test
    void actualizarEstado_shouldReturnOk() throws Exception {
        OrganizacionResponseDTO res = OrganizacionResponseDTO.builder().idOrganizacion(1L).estado("ACTIVA").build();
        when(organizacionService.actualizarEstado(any(Long.class), anyString())).thenReturn(res);

        mockMvc.perform(put("/mascotas/v1/organizaciones/1/estado")
                .param("estado", "ACTIVA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ACTIVA"));
    }
}
