package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.UsuarioRequestDTO;
import com.sanosysalvos.mascotas.dto.UsuarioResponseDTO;
import com.sanosysalvos.mascotas.entity.Organizacion;
import com.sanosysalvos.mascotas.entity.TipoCuenta;
import com.sanosysalvos.mascotas.entity.Usuario;
import com.sanosysalvos.mascotas.exception.ResourceNotFoundException;
import com.sanosysalvos.mascotas.repository.OrganizacionRepository;
import com.sanosysalvos.mascotas.repository.TipoCuentaRepository;
import com.sanosysalvos.mascotas.repository.UsuarioRepository;
import com.sanosysalvos.mascotas.service.RabbitMQProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoCuentaRepository tipoCuentaRepository;

    @Mock
    private OrganizacionRepository organizacionRepository;

    @Mock
    private RabbitMQProducer rabbitMQProducer;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void registrarUsuario_shouldCreateAndReturnDto() {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setAuth0Id("auth0|123");
        req.setNombre("Claudio");
        req.setIdTipoCuenta(1L);

        when(usuarioRepository.existsByAuth0Id("auth0|123")).thenReturn(false);
        
        TipoCuenta tc = new TipoCuenta();
        tc.setIdTipoCuenta(1L);
        tc.setDescripcion("Normal");
        when(tipoCuentaRepository.findById(1L)).thenReturn(Optional.of(tc));

        Usuario savedUser = Usuario.builder()
                .idUsuario(1L)
                .auth0Id("auth0|123")
                .nombre("Claudio")
                .tipoCuenta(tc)
                .estadoMembresia("NINGUNO")
                .build();

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUser);

        UsuarioResponseDTO res = usuarioService.registrarUsuario(req);

        assertNotNull(res);
        assertEquals(1L, res.getIdUsuario());
        assertEquals("auth0|123", res.getAuth0Id());
        assertEquals("Normal", res.getDescripcionTipoCuenta());
    }

    @Test
    void obtenerPerfilPorAuth0Id_shouldReturnDto() {
        Usuario u = Usuario.builder().idUsuario(1L).auth0Id("auth0|123").build();
        when(usuarioRepository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(u));

        UsuarioResponseDTO res = usuarioService.obtenerPerfilPorAuth0Id("auth0|123");
        assertNotNull(res);
        assertEquals(1L, res.getIdUsuario());
    }

    @Test
    void actualizarUsuario_shouldUpdateAndNotify() {
        Usuario existingUser = Usuario.builder()
                .idUsuario(1L)
                .auth0Id("auth0|123")
                .nombre("Claudio")
                .tipoCuenta(new TipoCuenta(1L, "Normal"))
                .build();
        
        when(usuarioRepository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(existingUser));

        Organizacion newOrg = new Organizacion();
        newOrg.setIdOrganizacion(10L);
        when(organizacionRepository.findById(10L)).thenReturn(Optional.of(newOrg));

        Usuario adminUser = Usuario.builder().idUsuario(2L).organizacion(newOrg).tipoCuenta(new TipoCuenta(2L, "Admin")).build();
        when(usuarioRepository.findAll()).thenReturn(List.of(adminUser));

        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNombre("Claudio Modificado");
        req.setIdOrganizacion(10L);

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        UsuarioResponseDTO res = usuarioService.actualizarUsuario("auth0|123", req);

        assertEquals("Claudio Modificado", res.getNombre());
        assertEquals("PENDIENTE", res.getEstadoMembresia());
        verify(rabbitMQProducer, times(1)).enviarNotificacion(anyLong(), anyString(), anyString(), anyString(), anyString());
    }
}
