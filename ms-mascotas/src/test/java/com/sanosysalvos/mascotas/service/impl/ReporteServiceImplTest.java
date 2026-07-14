package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.FiltroBusquedaMasivaDTO;
import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.repository.*;
import com.sanosysalvos.mascotas.service.MascotasIntegrationService;
import com.sanosysalvos.mascotas.service.ReporteFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceImplTest {

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
    private MascotasIntegrationService integrationService;
    @Mock
    private ReporteFactory reporteFactory;

    @InjectMocks
    private ReporteServiceImpl reporteService;

    private Usuario usuario;
    private Mascota mascota;
    private Reporte reporte;
    private ReporteRequestDTO request;
    private ReporteResponseDTO response;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setAuth0Id("auth0|123");

        mascota = new Mascota();
        mascota.setIdMascota(1L);

        reporte = new Reporte();
        reporte.setIdReporte(1L);
        reporte.setUsuario(usuario);
        reporte.setMascota(mascota);

        request = new ReporteRequestDTO();
        request.setIdMascota(1L);
        request.setIdUbicacionReporte(1L);
        request.setIdTipoReporte(1L);
        request.setFechaIncidente(LocalDateTime.now());

        response = new ReporteResponseDTO();
        response.setIdReporte(1L);
    }

    @Test
    void crearReporteExito() {
        when(usuarioRepository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(usuario));
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(integrationService.obtenerUbicacion(1L)).thenReturn(ResponseEntity.ok().build());
        when(reporteFactory.crearReporte(request, usuario, mascota)).thenReturn(reporte);
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);
        when(reporteFactory.mapearAResponse(reporte)).thenReturn(response);

        ReporteResponseDTO result = reporteService.crearReporte(request, "auth0|123");

        assertNotNull(result);
        assertEquals(1L, result.getIdReporte());
        verify(integrationService).procesarReporteTrigger(1L);
    }

    @Test
    void crearReporteUsuarioNoEncontrado() {
        when(usuarioRepository.findByAuth0Id("auth0|999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> 
            reporteService.crearReporte(request, "auth0|999")
        );

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void crearReporteMascotaNoEncontrada() {
        when(usuarioRepository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(usuario));
        when(mascotaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> 
            reporteService.crearReporte(request, "auth0|123")
        );

        assertEquals("Mascota no encontrada", exception.getMessage());
    }

    @Test
    void obtenerTodosLosReportesExito() {
        when(reporteRepository.findAll()).thenReturn(Arrays.asList(reporte));
        when(reporteFactory.mapearAResponse(reporte)).thenReturn(response);

        List<ReporteResponseDTO> result = reporteService.obtenerTodosLosReportes();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void obtenerReportePorIdExito() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteFactory.mapearAResponse(reporte)).thenReturn(response);

        ReporteResponseDTO result = reporteService.obtenerReportePorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdReporte());
    }

    @Test
    void cerrarReporteExito() {
        EstadoReporte estadoCerrado = new EstadoReporte();
        estadoCerrado.setIdEstadoReporte(2L);
        estadoCerrado.setDescripcion("Cerrado/Resuelto");

        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(estadoReporteRepository.findByDescripcion("Cerrado/Resuelto")).thenReturn(estadoCerrado);
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);
        when(reporteFactory.mapearAResponse(reporte)).thenReturn(response);

        ReporteResponseDTO result = reporteService.cerrarReporte(1L, "auth0|123");

        assertNotNull(result);
        verify(reporteRepository).save(reporte);
    }

    @Test
    void cerrarReporteSinPermisos() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));

        Exception exception = assertThrows(RuntimeException.class, () -> 
            reporteService.cerrarReporte(1L, "auth0|other")
        );

        assertEquals("No tiene permisos para cerrar este reporte", exception.getMessage());
    }

    @Test
    void actualizarReporteExito() {
        TipoReporte tipoReporte = new TipoReporte();
        tipoReporte.setIdTipoReporte(1L);

        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(tipoReporteRepository.findById(1L)).thenReturn(Optional.of(tipoReporte));
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);
        when(reporteFactory.mapearAResponse(reporte)).thenReturn(response);

        ReporteResponseDTO result = reporteService.actualizarReporte(1L, request, "auth0|123");

        assertNotNull(result);
        verify(reporteRepository).save(reporte);
        verify(integrationService).procesarReporteTrigger(1L);
    }

    @Test
    void eliminarReporteExito() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));

        assertDoesNotThrow(() -> reporteService.eliminarReporte(1L, "auth0|123"));
        verify(reporteRepository).delete(reporte);
    }

    @Test
    void buscarReportesCandidatosExito() {
        FiltroBusquedaMasivaDTO filtro = new FiltroBusquedaMasivaDTO();
        filtro.setTipoReporteBuscado("Extraviado");
        filtro.setEspecie("Perro");
        filtro.setUbicacionesIds(Collections.singletonList(1L));

        when(reporteRepository.findByEstadoReporte_DescripcionIgnoreCaseAndTipoReporte_DescripcionIgnoreCaseAndMascota_Raza_Especie_NombreEspecieIgnoreCaseAndIdUbicacionReporteIn(
                "Activo", "Extraviado", "Perro", filtro.getUbicacionesIds()))
            .thenReturn(Arrays.asList(reporte));
        when(reporteFactory.mapearAResponse(reporte)).thenReturn(response);

        List<ReporteResponseDTO> result = reporteService.buscarReportesCandidatos(filtro);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
