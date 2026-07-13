package com.sanosysalvos.mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.mascotas.dto.UsuarioRequestDTO;
import com.sanosysalvos.mascotas.dto.UsuarioResponseDTO;
import com.sanosysalvos.mascotas.service.UsuarioService;
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
class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void registrarUsuario_shouldReturnCreated() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setAuth0Id("auth0|123");
        req.setNombre("Claudio");
        req.setEmail("claudio@test.com");
        req.setTelefono("+56912345678");

        UsuarioResponseDTO res = UsuarioResponseDTO.builder()
                .idUsuario(1L)
                .auth0Id("auth0|123")
                .nombre("Claudio")
                .build();

        when(usuarioService.registrarUsuario(any(UsuarioRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/mascotas/v1/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nombre").value("Claudio"));
    }

    @Test
    void obtenerMiPerfil_shouldReturnOk() throws Exception {
        UsuarioResponseDTO res = UsuarioResponseDTO.builder().idUsuario(1L).auth0Id("auth0|123").build();
        when(usuarioService.obtenerPerfilPorAuth0Id("auth0|123")).thenReturn(res);

        mockMvc.perform(get("/mascotas/v1/usuarios/me")
                .header("X-Auth0-Id", "auth0|123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1));
    }

    @Test
    void actualizarMiPerfil_shouldReturnNoContent() throws Exception {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setAuth0Id("auth0|123");
        req.setNombre("Claudio");
        req.setEmail("claudio@test.com");
        req.setTelefono("+56912345678");
        
        when(usuarioService.actualizarUsuario(anyString(), any(UsuarioRequestDTO.class))).thenReturn(null);

        mockMvc.perform(put("/mascotas/v1/usuarios/me")
                .header("X-Auth0-Id", "auth0|123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarMiPerfil_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/mascotas/v1/usuarios/me")
                .header("X-Auth0-Id", "auth0|123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
