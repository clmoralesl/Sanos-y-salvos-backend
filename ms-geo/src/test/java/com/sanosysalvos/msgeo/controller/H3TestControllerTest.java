package com.sanosysalvos.msgeo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class H3TestControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private H3TestController h3TestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(h3TestController).build();
    }

    @Test
    void obtenerIndice_shouldReturnIndex() throws Exception {
        mockMvc.perform(get("/geo/v1/h3-test/indice")
                .param("latitud", "-33.4")
                .param("longitud", "-70.6")
                .param("resolucion", "9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitud").value(-33.4))
                .andExpect(jsonPath("$.longitud").value(-70.6))
                .andExpect(jsonPath("$.resolucion").value(9))
                .andExpect(jsonPath("$.indiceHexagonal").exists());
    }
}
