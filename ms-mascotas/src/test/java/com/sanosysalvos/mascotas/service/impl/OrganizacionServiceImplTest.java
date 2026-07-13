package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.OrganizacionRequestDTO;
import com.sanosysalvos.mascotas.dto.OrganizacionResponseDTO;
import com.sanosysalvos.mascotas.entity.Organizacion;
import com.sanosysalvos.mascotas.repository.OrganizacionRepository;
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

        OrganizacionResponseDTO res = organizacionService.crearOrganizacion(req);
        
        assertNotNull(res);
        assertEquals(1L, res.getIdOrganizacion());
        assertEquals("Org Test", res.getNombreOrganizacion());
    }

    @Test
    void obtenerOrganizacionPorId_shouldReturnDto() {
        Organizacion org = new Organizacion();
        org.setIdOrganizacion(1L);
        when(organizacionRepository.findById(1L)).thenReturn(Optional.of(org));

        OrganizacionResponseDTO res = organizacionService.obtenerOrganizacionPorId(1L);
        assertEquals(1L, res.getIdOrganizacion());
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

        OrganizacionResponseDTO res = organizacionService.actualizarOrganizacion(1L, req);
        assertEquals("New", res.getNombreOrganizacion());
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
}
