package com.sanosysalvos.mascotas.controller;

import com.sanosysalvos.mascotas.dto.CatalogoResponseDTO;
import com.sanosysalvos.mascotas.service.CatalogoService;
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
class CatalogoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CatalogoService catalogoService;

    @InjectMocks
    private CatalogoController catalogoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(catalogoController).build();
    }

    @Test
    void obtenerRazas_shouldReturnList() throws Exception {
        when(catalogoService.obtenerRazas()).thenReturn(List.of(CatalogoResponseDTO.builder().id(1L).descripcion("Raza1").build()));

        mockMvc.perform(get("/mascotas/v1/catalogos/razas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void obtenerEspecies_shouldReturnList() throws Exception {
        when(catalogoService.obtenerEspecies()).thenReturn(List.of(CatalogoResponseDTO.builder().id(1L).descripcion("Perro").build()));

        mockMvc.perform(get("/mascotas/v1/catalogos/especies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void obtenerTamanios_shouldReturnList() throws Exception {
        when(catalogoService.obtenerTamanios()).thenReturn(List.of(CatalogoResponseDTO.builder().id(1L).descripcion("Grande").build()));

        mockMvc.perform(get("/mascotas/v1/catalogos/tamanios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void obtenerCaracteristicas_shouldReturnList() throws Exception {
        when(catalogoService.obtenerCaracteristicas()).thenReturn(List.of(CatalogoResponseDTO.builder().id(1L).descripcion("Amigable").build()));

        mockMvc.perform(get("/mascotas/v1/catalogos/caracteristicas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void obtenerTiposReporte_shouldReturnList() throws Exception {
        when(catalogoService.obtenerTiposReporte()).thenReturn(List.of(CatalogoResponseDTO.builder().id(1L).descripcion("Perdido").build()));

        mockMvc.perform(get("/mascotas/v1/catalogos/tipos-reporte")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void obtenerTiposCuenta_shouldReturnList() throws Exception {
        when(catalogoService.obtenerTiposCuenta()).thenReturn(List.of(CatalogoResponseDTO.builder().id(1L).descripcion("Premium").build()));

        mockMvc.perform(get("/mascotas/v1/catalogos/tipos-cuenta")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
