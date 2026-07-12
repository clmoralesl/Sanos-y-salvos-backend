package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.repository.*;
import com.sanosysalvos.mascotas.client.CoincidenciaClient;
import com.sanosysalvos.mascotas.client.UbicacionClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReporteServiceImplTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private TipoReporteRepository tipoReporteRepository;

    @Mock
    private EstadoReporteRepository estadoReporteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private CoincidenciaClient coincidenciaClient;

    @Mock
    private UbicacionClient ubicacionClient;

    @InjectMocks
    private ReporteServiceImpl service;

    @Test
    public void testCrearReporteUsuarioNotFound() {
        ReporteRequestDTO request = ReporteRequestDTO.builder().build();
        when(usuarioRepository.findByAuth0Id("auth0")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.crearReporte(request, "auth0"));
    }

    @Test
    public void testCrearReporteUbicacionFails() {
        ReporteRequestDTO request = ReporteRequestDTO.builder()
                .idMascota(1L)
                .idTipoReporte(2L)
                .idUbicacionReporte(3L)
                .build();

        Usuario usuario = Usuario.builder().idUsuario(1L).auth0Id("auth0").build();
        Mascota mascota = Mascota.builder().idMascota(1L).build();
        TipoReporte tipo = TipoReporte.builder().idTipoReporte(2L).descripcion("Perdida").build();

        when(usuarioRepository.findByAuth0Id("auth0")).thenReturn(Optional.of(usuario));
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(tipoReporteRepository.findById(2L)).thenReturn(Optional.of(tipo));
        when(ubicacionClient.obtenerUbicacion(3L)).thenThrow(new RuntimeException("Conexión perdida"));

        assertThrows(RuntimeException.class, () -> service.crearReporte(request, "auth0"));
    }

    @Test
    public void testCrearReporteSuccess() {
        ReporteRequestDTO request = ReporteRequestDTO.builder()
                .idMascota(1L)
                .idTipoReporte(2L)
                .idUbicacionReporte(3L)
                .fechaIncidente(LocalDateTime.now())
                .build();

        Usuario usuario = Usuario.builder().idUsuario(1L).auth0Id("auth0").build();
        Mascota mascota = Mascota.builder().idMascota(1L).build();
        TipoReporte tipo = TipoReporte.builder().idTipoReporte(2L).descripcion("Perdida").build();
        EstadoReporte estado = EstadoReporte.builder().idEstadoReporte(1L).descripcion("Activo").build();

        when(usuarioRepository.findByAuth0Id("auth0")).thenReturn(Optional.of(usuario));
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(tipoReporteRepository.findById(2L)).thenReturn(Optional.of(tipo));
        when(ubicacionClient.obtenerUbicacion(3L)).thenReturn(ResponseEntity.ok().build());
        when(estadoReporteRepository.findByDescripcion("Activo")).thenReturn(estado);

        Reporte savedReport = Reporte.builder()
                .idReporte(100L)
                .fechaRegistro(LocalDateTime.now())
                .fechaIncidente(request.getFechaIncidente())
                .idUbicacionReporte(3L)
                .tipoReporte(tipo)
                .estadoReporte(estado)
                .usuario(usuario)
                .mascota(mascota)
                .build();

        when(reporteRepository.save(any(Reporte.class))).thenReturn(savedReport);

        ReporteResponseDTO response = service.crearReporte(request, "auth0");

        assertNotNull(response);
        assertEquals(100L, response.getIdReporte());
        assertEquals("Activo", response.getEstadoReporte());
    }
}
