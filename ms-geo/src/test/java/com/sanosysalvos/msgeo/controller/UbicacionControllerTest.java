package com.sanosysalvos.msgeo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.msgeo.dto.UbicacionRequestDTO;
import com.sanosysalvos.msgeo.model.UbicacionReporte;
import com.sanosysalvos.msgeo.service.UbicacionService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UbicacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UbicacionService ubicacionService;

    @InjectMocks
    private UbicacionController ubicacionController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ubicacionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void crearUbicacion_shouldReturnCreated() throws Exception {
        UbicacionRequestDTO request = new UbicacionRequestDTO();
        request.setIdComuna(1L);
        request.setLatitud(-33.4489);
        request.setLongitud(-70.6693);
        request.setDireccionEspecifica("Av. Libertador Bernardo O'Higgins 123");

        when(ubicacionService.crearUbicacion(any(UbicacionRequestDTO.class))).thenReturn(100L);

        mockMvc.perform(post("/geo/v1/ubicaciones")
                .header("X-Auth0-Id", "auth0|123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Ubicación guardada correctamente"))
                .andExpect(jsonPath("$.idUbicacion").value(100));
    }

    @Test
    void obtenerUbicacion_whenExists_shouldReturnOk() throws Exception {
        UbicacionReporte ubicacion = UbicacionReporte.builder().id(100L).latitud(-33.4).build();
        when(ubicacionService.obtenerUbicacionPorId(100L)).thenReturn(ubicacion);

        mockMvc.perform(get("/geo/v1/ubicaciones/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.latitud").value(-33.4));
    }

    @Test
    void obtenerUbicacion_whenNotFound_shouldReturn404() throws Exception {
        when(ubicacionService.obtenerUbicacionPorId(100L)).thenReturn(null);

        mockMvc.perform(get("/geo/v1/ubicaciones/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerUbicacionesCercanas_shouldReturnList() throws Exception {
        when(ubicacionService.obtenerUbicacionesEnRadio(eq(100L), anyInt())).thenReturn(List.of(200L, 300L));

        mockMvc.perform(get("/geo/v1/ubicaciones/100/cercanas")
                .param("radio", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(200))
                .andExpect(jsonPath("$[1]").value(300));
    }
}
