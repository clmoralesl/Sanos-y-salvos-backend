package com.sanosysalvos.msgeo.controller;

import com.sanosysalvos.msgeo.model.Comuna;
import com.sanosysalvos.msgeo.model.Region;
import com.sanosysalvos.msgeo.service.CatalogoGeoService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CatalogoGeoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CatalogoGeoService catalogoGeoService;

    @InjectMocks
    private CatalogoGeoController catalogoGeoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(catalogoGeoController).build();
    }

    @Test
    void obtenerRegiones_shouldReturnRegiones() throws Exception {
        Region region = new Region();
        region.setId(1L);
        region.setNombre("Metropolitana");

        when(catalogoGeoService.obtenerRegiones()).thenReturn(List.of(region));

        mockMvc.perform(get("/geo/v1/catalogos-geo/regiones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Metropolitana"));
    }

    @Test
    void obtenerComunasPorRegion_shouldReturnComunas() throws Exception {
        Comuna comuna = new Comuna();
        comuna.setId(1L);
        comuna.setNombre("Santiago");

        when(catalogoGeoService.obtenerComunasPorRegion(1L)).thenReturn(List.of(comuna));

        mockMvc.perform(get("/geo/v1/catalogos-geo/regiones/1/comunas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Santiago"));
    }
}
