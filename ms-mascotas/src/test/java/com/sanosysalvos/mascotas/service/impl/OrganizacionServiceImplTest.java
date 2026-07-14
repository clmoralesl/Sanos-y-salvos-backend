package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.OrganizacionRequestDTO;
import com.sanosysalvos.mascotas.dto.OrganizacionResponseDTO;
import com.sanosysalvos.mascotas.entity.Organizacion;
import com.sanosysalvos.mascotas.entity.TipoCuenta;
import com.sanosysalvos.mascotas.entity.Usuario;
import com.sanosysalvos.mascotas.repository.OrganizacionRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizacionServiceImplTest {

    @Mock
    private OrganizacionRepository organizacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RabbitMQProducer rabbitMQProducer;

    @InjectMocks
    private OrganizacionServiceImpl organizacionService;

    @Test
    void crearOrganizacion_shouldCreateAndNotify() {
        OrganizacionRequestDTO req = new OrganizacionRequestDTO();
        req.setNombreOrganizacion("Org Test");

        Organizacion org = new Organizacion();
        org.setIdOrganizacion(1L);
        org.setNombreOrganizacion("Org Test");
        org.setEstado("PENDIENTE");

        when(organizacionRepository.save(any(Organizacion.class))).thenReturn(org);
        when(usuarioRepository.findAll()).thenReturn(List.of());
        when(usuarioRepository.findByOrganizacionIdOrganizacion(1L)).thenReturn(List.of());

        OrganizacionResponseDTO res = organizacionService.crearOrganizacion(req);

        assertNotNull(res);
        assertEquals(1L, res.getIdOrganizacion());
        assertEquals("Org Test", res.getNombreOrganizacion());
    }

    @Test
    void crearOrganizacion_shouldNotifySuperAdmins() {
        OrganizacionRequestDTO req = new OrganizacionRequestDTO();
        req.setNombreOrganizacion("Org con SuperAdmin");

        Organizacion org = new Organizacion();
        org.setIdOrganizacion(2L);
        org.setNombreOrganizacion("Org con SuperAdmin");

        TipoCuenta superAdminTc = new TipoCuenta(3L, "SUPER_ADMIN");
        Usuario superAdmin = Usuario.builder().idUsuario(99L).tipoCuenta(superAdminTc).build();

        when(organizacionRepository.save(any(Organizacion.class))).thenReturn(org);
        when(usuarioRepository.findAll()).thenReturn(List.of(superAdmin));
        when(usuarioRepository.findByOrganizacionIdOrganizacion(2L)).thenReturn(List.of());

        organizacionService.crearOrganizacion(req);

        verify(rabbitMQProducer).enviarNotificacion(eq(99L), anyString(), anyString(), eq("ALERTA"), anyString());
    }

    @Test
    void obtenerOrganizacionPorId_shouldReturnDto() {
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(1L);
        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(org));
        when(usuarioRepository.findByOrganizacionIdOrganizacion(1L)).thenReturn(List.of());

        OrganizacionResponseDTO res = organizacionService.obtenerOrganizacionPorId(1L);
        assertEquals(1L, res.getIdOrganizacion());
    }

    @Test
    void obtenerOrganizacionPorId_shouldThrowWhenNotFound() {
        when(organizacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> organizacionService.obtenerOrganizacionPorId(99L));
    }

    @Test
    void obtenerTodas_shouldReturnList() {
        Organizacion o1 = new Organizacion();
        o1.setIdOrganizacion(1L);
        Organizacion o2 = new Organizacion();
        o2.setIdOrganizacion(2L);

        when(organizacionRepository.findAll()).thenReturn(List.of(o1, o2));
        when(usuarioRepository.findByOrganizacionIdOrganizacion(1L)).thenReturn(List.of());
        when(usuarioRepository.findByOrganizacionIdOrganizacion(2L)).thenReturn(List.of());

        List<OrganizacionResponseDTO> result = organizacionService.obtenerTodas();

        assertEquals(2, result.size());
    }

    @Test
    void actualizarOrganizacion_shouldUpdate() {
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(1L);
        org.setNombreOrganizacion("Old");

        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(org));

        OrganizacionRequestDTO req = new OrganizacionRequestDTO();
        req.setNombreOrganizacion("New");

        when(organizacionRepository.save(any(Organizacion.class))).thenAnswer(i -> i.getArguments()[0]);
        when(usuarioRepository.findByOrganizacionIdOrganizacion(1L)).thenReturn(List.of());

        OrganizacionResponseDTO res = organizacionService.actualizarOrganizacion(1L, req);
        assertEquals("New", res.getNombreOrganizacion());
    }

    @Test
    void actualizarOrganizacion_shouldThrowWhenNotFound() {
        when(organizacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> organizacionService.actualizarOrganizacion(99L, new OrganizacionRequestDTO()));
    }

    @Test
    void eliminarOrganizacion_shouldDeleteWhenFound() {
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(1L);
        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(org));

        organizacionService.eliminarOrganizacion(1L);

        verify(organizacionRepository).delete(org);
    }

    @Test
    void eliminarOrganizacion_shouldThrowWhenNotFound() {
        when(organizacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> organizacionService.eliminarOrganizacion(99L));
    }

    @Test
    void actualizarEstado_shouldUpdateStateAndNotify() {
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(1L);
        org.setEstado("PENDIENTE");

        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(org));
        when(organizacionRepository.save(any(Organizacion.class))).thenAnswer(i -> i.getArguments()[0]);
        when(usuarioRepository.findByOrganizacionIdOrganizacion(1L)).thenReturn(List.of());

        OrganizacionResponseDTO res = organizacionService.actualizarEstado(1L, "ACTIVA");
        assertEquals("ACTIVA", res.getEstado());
    }

    @Test
    void actualizarEstado_activa_shouldApproveAdminOrgUsersAndNotify() {
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(1L);
        org.setNombreOrganizacion("Org Aprobada");

        TipoCuenta adminOrgTc = new TipoCuenta(2L, "ADMIN_ORG");
        Usuario adminOrg = Usuario.builder()
                .idUsuario(10L)
                .tipoCuenta(adminOrgTc)
                .organizacion(org)
                .estadoMembresia("PENDIENTE")
                .build();

        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(org));
        when(organizacionRepository.save(any(Organizacion.class))).thenAnswer(i -> i.getArguments()[0]);
        when(usuarioRepository.findByOrganizacionIdOrganizacion(1L)).thenReturn(List.of(adminOrg));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        organizacionService.actualizarEstado(1L, "ACTIVA");

        assertEquals("APROBADO", adminOrg.getEstadoMembresia());
        verify(rabbitMQProducer).enviarNotificacion(eq(10L), eq("Organización Aprobada"), anyString(), eq("SISTEMA"), anyString());
    }

    @Test
    void actualizarEstado_rechazada_shouldDisassociateUsersAndNotifyAdmins() {
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(1L);
        org.setNombreOrganizacion("Org Rechazada");

        TipoCuenta adminOrgTc = new TipoCuenta(2L, "ADMIN_ORG");
        Usuario adminOrg = Usuario.builder()
                .idUsuario(20L)
                .tipoCuenta(adminOrgTc)
                .organizacion(org)
                .estadoMembresia("PENDIENTE")
                .build();

        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(org));
        when(organizacionRepository.save(any(Organizacion.class))).thenAnswer(i -> i.getArguments()[0]);
        when(usuarioRepository.findByOrganizacionIdOrganizacion(1L)).thenReturn(List.of(adminOrg));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        organizacionService.actualizarEstado(1L, "RECHAZADA");

        assertNull(adminOrg.getOrganizacion());
        assertEquals("NINGUNO", adminOrg.getEstadoMembresia());
        verify(rabbitMQProducer).enviarNotificacion(eq(20L), eq("Organización Rechazada"), anyString(), eq("SISTEMA"), anyString());
    }

    @Test
    void actualizarEstado_shouldThrowWhenNotFound() {
        when(organizacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> organizacionService.actualizarEstado(99L, "ACTIVA"));
    }

    @Test
    void mapToDTO_shouldIncludeRepresentanteDataFromAdminOrg() {
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(1L);
        org.setNombreOrganizacion("Org Con Admin");

        TipoCuenta adminOrgTc = new TipoCuenta(2L, "ADMIN_ORG");
        Usuario adminOrg = Usuario.builder()
                .idUsuario(5L)
                .nombre("Rep Nombre")
                .email("rep@email.com")
                .telefono("+56911111111")
                .tipoCuenta(adminOrgTc)
                .build();

        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(org));
        when(usuarioRepository.findByOrganizacionIdOrganizacion(1L)).thenReturn(List.of(adminOrg));

        OrganizacionResponseDTO res = organizacionService.obtenerOrganizacionPorId(1L);

        assertEquals("Rep Nombre", res.getNombreRepresentante());
        assertEquals("rep@email.com", res.getEmailRepresentante());
        assertEquals("+56911111111", res.getTelefonoRepresentante());
    }
}
