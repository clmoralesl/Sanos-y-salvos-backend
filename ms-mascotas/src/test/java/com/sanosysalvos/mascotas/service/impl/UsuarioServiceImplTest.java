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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void registrarUsuario_shouldThrowWhenAuth0IdAlreadyExists() {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setAuth0Id("auth0|existing");

        when(usuarioRepository.existsByAuth0Id("auth0|existing")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> usuarioService.registrarUsuario(req));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrarUsuario_shouldThrowWhenTipoCuentaNotFound() {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setAuth0Id("auth0|123");
        req.setIdTipoCuenta(99L);

        when(usuarioRepository.existsByAuth0Id(anyString())).thenReturn(false);
        when(tipoCuentaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.registrarUsuario(req));
    }

    @Test
    void registrarUsuario_withOrganizacion_setsEstadoPendiente() {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setAuth0Id("auth0|456");
        req.setNombre("Maria");
        req.setIdTipoCuenta(1L);
        req.setIdOrganizacion(5L);

        TipoCuenta tc = new TipoCuenta(1L, "Normal");
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(5L);

        when(usuarioRepository.existsByAuth0Id("auth0|456")).thenReturn(false);
        when(tipoCuentaRepository.findById(1L)).thenReturn(Optional.of(tc));
        when(organizacionRepository.findById(5L)).thenReturn(Optional.of(org));

        Usuario saved = Usuario.builder()
                .idUsuario(2L)
                .auth0Id("auth0|456")
                .tipoCuenta(tc)
                .organizacion(org)
                .estadoMembresia("PENDIENTE")
                .build();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(saved);

        UsuarioResponseDTO res = usuarioService.registrarUsuario(req);

        assertEquals("PENDIENTE", res.getEstadoMembresia());
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
    void obtenerPerfilPorAuth0Id_shouldThrowWhenNotFound() {
        when(usuarioRepository.findByAuth0Id("auth0|missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.obtenerPerfilPorAuth0Id("auth0|missing"));
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

    @Test
    void actualizarUsuario_shouldThrowWhenUserNotFound() {
        when(usuarioRepository.findByAuth0Id("auth0|missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.actualizarUsuario("auth0|missing", new UsuarioRequestDTO()));
    }

    @Test
    void actualizarUsuario_withSameOrganizacion_shouldNotChangeMembresiaOrNotify() {
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(10L);

        Usuario existingUser = Usuario.builder()
                .idUsuario(1L)
                .auth0Id("auth0|123")
                .tipoCuenta(new TipoCuenta(1L, "Normal"))
                .organizacion(org)
                .estadoMembresia("APROBADO")
                .build();

        when(usuarioRepository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(existingUser));
        when(organizacionRepository.findById(10L)).thenReturn(Optional.of(org));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNombre("Mismo");
        req.setIdOrganizacion(10L);

        usuarioService.actualizarUsuario("auth0|123", req);

        verify(rabbitMQProducer, never()).enviarNotificacion(anyLong(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void eliminarUsuario_shouldDeleteWhenFound() {
        Usuario u = Usuario.builder().idUsuario(1L).auth0Id("auth0|123").build();
        when(usuarioRepository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(u));

        usuarioService.eliminarUsuario("auth0|123");

        verify(usuarioRepository).delete(u);
    }

    @Test
    void eliminarUsuario_shouldThrowWhenNotFound() {
        when(usuarioRepository.findByAuth0Id("auth0|none")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.eliminarUsuario("auth0|none"));
    }

    @Test
    void obtenerTodosLosUsuarios_shouldReturnList() {
        TipoCuenta tc = new TipoCuenta(1L, "Normal");
        Usuario u1 = Usuario.builder().idUsuario(1L).tipoCuenta(tc).build();
        Usuario u2 = Usuario.builder().idUsuario(2L).tipoCuenta(tc).build();
        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UsuarioResponseDTO> result = usuarioService.obtenerTodosLosUsuarios();

        assertEquals(2, result.size());
    }

    @Test
    void obtenerUsuarioPorId_shouldReturnDto() {
        TipoCuenta tc = new TipoCuenta(1L, "Normal");
        Usuario u = Usuario.builder().idUsuario(1L).tipoCuenta(tc).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        UsuarioResponseDTO res = usuarioService.obtenerUsuarioPorId(1L);

        assertEquals(1L, res.getIdUsuario());
    }

    @Test
    void obtenerUsuarioPorId_shouldThrowWhenNotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.obtenerUsuarioPorId(99L));
    }

    @Test
    void actualizarUsuarioPorId_shouldUpdateAllFields() {
        TipoCuenta tc = new TipoCuenta(1L, "Normal");
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(5L);

        Usuario existingUser = Usuario.builder()
                .idUsuario(1L)
                .tipoCuenta(tc)
                .organizacion(org)
                .estadoMembresia("APROBADO")
                .build();

        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setAuth0Id("auth0|new");
        req.setNombre("Nuevo Nombre");
        req.setEmail("nuevo@email.com");
        req.setTelefono("+56912345678");
        req.setIdTipoCuenta(1L);
        req.setIdOrganizacion(5L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(tipoCuentaRepository.findById(1L)).thenReturn(Optional.of(tc));
        when(organizacionRepository.findById(5L)).thenReturn(Optional.of(org));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        UsuarioResponseDTO res = usuarioService.actualizarUsuarioPorId(1L, req);

        assertEquals("Nuevo Nombre", res.getNombre());
    }

    @Test
    void actualizarUsuarioPorId_withoutOrganizacion_setsNinguno() {
        TipoCuenta tc = new TipoCuenta(1L, "Normal");
        Usuario existingUser = Usuario.builder()
                .idUsuario(1L)
                .tipoCuenta(tc)
                .estadoMembresia("APROBADO")
                .build();

        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setIdTipoCuenta(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(tipoCuentaRepository.findById(1L)).thenReturn(Optional.of(tc));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        UsuarioResponseDTO res = usuarioService.actualizarUsuarioPorId(1L, req);

        assertEquals("NINGUNO", res.getEstadoMembresia());
    }

    @Test
    void actualizarUsuarioPorId_shouldThrowWhenUserNotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.actualizarUsuarioPorId(99L, new UsuarioRequestDTO()));
    }

    @Test
    void eliminarUsuarioPorId_shouldDeleteWhenFound() {
        Usuario u = Usuario.builder().idUsuario(1L).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        usuarioService.eliminarUsuarioPorId(1L);

        verify(usuarioRepository).delete(u);
    }

    @Test
    void eliminarUsuarioPorId_shouldThrowWhenNotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.eliminarUsuarioPorId(99L));
    }

    @Test
    void actualizarMembresia_shouldSetAprobadoAndNotify() {
        TipoCuenta tc = new TipoCuenta(1L, "Normal");
        Usuario u = Usuario.builder().idUsuario(1L).tipoCuenta(tc).build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        UsuarioResponseDTO res = usuarioService.actualizarMembresia(1L, "APROBADO");

        assertEquals("APROBADO", res.getEstadoMembresia());
        verify(rabbitMQProducer).enviarNotificacion(eq(1L), eq("Membresía Aprobada"), anyString(), eq("SISTEMA"), eq("/perfil"));
    }

    @Test
    void actualizarMembresia_shouldSetRechazadoAndNotify() {
        TipoCuenta tc = new TipoCuenta(1L, "Normal");
        Usuario u = Usuario.builder().idUsuario(2L).tipoCuenta(tc).build();

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        UsuarioResponseDTO res = usuarioService.actualizarMembresia(2L, "RECHAZADO");

        assertEquals("RECHAZADO", res.getEstadoMembresia());
        verify(rabbitMQProducer).enviarNotificacion(eq(2L), eq("Membresía Rechazada"), anyString(), eq("SISTEMA"), eq("/perfil"));
    }

    @Test
    void actualizarMembresia_shouldThrowWhenUserNotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.actualizarMembresia(99L, "APROBADO"));
    }
}
