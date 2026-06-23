package com.sanosysalvos.msgeo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.msgeo.dto.UbicacionRequestDTO;
import com.sanosysalvos.msgeo.service.UbicacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UbicacionController.class)
public class UbicacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UbicacionService ubicacionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearUbicacionSuccess() throws Exception {
        UbicacionRequestDTO request = new UbicacionRequestDTO();
        request.setIdComuna(1L);
        request.setLatitud(-33.4);
        request.setLongitud(-70.6);

        when(ubicacionService.crearUbicacion(any(UbicacionRequestDTO.class))).thenReturn(100L);

        mockMvc.perform(post("/geo/v1/ubicaciones")
                        .header("X-Auth0-Id", "auth0|123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUbicacion").value(100L))
                .andExpect(jsonPath("$.message").value("Ubicación guardada correctamente"));
    }

    @Test
    public void testObtenerUbicacionesCercanas() throws Exception {
        when(ubicacionService.obtenerUbicacionesEnRadio(eq(1L), eq(3))).thenReturn(Arrays.asList(10L, 20L));

        mockMvc.perform(get("/geo/v1/ubicaciones/1/cercanas")
                        .param("radio", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(10L))
                .andExpect(jsonPath("$[1]").value(20L));
    }
}
