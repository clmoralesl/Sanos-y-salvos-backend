package com.sanosysalvos.coincidencias.service.impl;

import com.sanosysalvos.coincidencias.business.MotorSimilitud;
import com.sanosysalvos.coincidencias.domain.entity.Coincidencia;
import com.sanosysalvos.coincidencias.domain.enums.EstadoCoincidencia;
import com.sanosysalvos.coincidencias.integration.client.GeoClient;
import com.sanosysalvos.coincidencias.integration.client.MascotasClient;
import com.sanosysalvos.coincidencias.integration.dto.FiltroBusquedaMasivaDTO;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import com.sanosysalvos.coincidencias.integration.dto.ReporteDTO;
import com.sanosysalvos.coincidencias.repository.CoincidenciaRepository;
import com.sanosysalvos.coincidencias.service.RabbitMQProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoincidenciaServiceImplTest {

    @Mock
    private MascotasClient mascotasClient;

    @Mock
    private GeoClient geoClient;

    @Mock
    private MotorSimilitud motorSimilitud;

    @Mock
    private CoincidenciaRepository repository;

    @Mock
    private RabbitMQProducer rabbitMQProducer;

    @InjectMocks
    private CoincidenciaServiceImpl coincidenciaService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(coincidenciaService, "radioBusqueda", 6);
    }

    @Test
    void procesarReporte_reporteNoExiste() {
        when(mascotasClient.obtenerReportePorId(1L)).thenReturn(null);

        coincidenciaService.procesarReporte(1L);

        verify(geoClient, never()).obtenerUbicacionesCercanas(anyLong(), anyInt());
        verify(repository, never()).save(any());
    }

    @Test
    void procesarReporte_sinMascotaAsociada() {
        ReporteDTO reporte = new ReporteDTO();
        reporte.setIdUbicacionReporte(100L);

        when(mascotasClient.obtenerReportePorId(1L)).thenReturn(reporte);

        coincidenciaService.procesarReporte(1L);

        verify(geoClient, never()).obtenerUbicacionesCercanas(anyLong(), anyInt());
    }

    @Test
    void procesarReporte_mascotaBaseNoExiste() {
        ReporteDTO reporte = new ReporteDTO();
        reporte.setIdUbicacionReporte(100L);
        reporte.setIdMascota(10L);

        when(mascotasClient.obtenerReportePorId(1L)).thenReturn(reporte);
        when(mascotasClient.obtenerMascotaPorId(10L)).thenReturn(null);

        coincidenciaService.procesarReporte(1L);

        verify(geoClient, never()).obtenerUbicacionesCercanas(anyLong(), anyInt());
    }

    @Test
    void procesarReporte_sinUbicacionesCercanas() {
        ReporteDTO reporte = new ReporteDTO();
        reporte.setIdUbicacionReporte(100L);
        reporte.setIdMascota(10L);
        MascotaDTO mascota = new MascotaDTO();

        when(mascotasClient.obtenerReportePorId(1L)).thenReturn(reporte);
        when(mascotasClient.obtenerMascotaPorId(10L)).thenReturn(mascota);
        when(geoClient.obtenerUbicacionesCercanas(100L, 6)).thenReturn(Collections.emptyList());

        coincidenciaService.procesarReporte(1L);

        verify(mascotasClient, never()).buscarReportesCandidatos(any());
    }

    @Test
    void procesarReporte_conCoincidencia() {
        ReporteDTO reporteBase = new ReporteDTO();
        reporteBase.setIdReporte(1L);
        reporteBase.setIdUbicacionReporte(100L);
        reporteBase.setIdMascota(10L);
        reporteBase.setTipoReporte("Mascota Perdida");
        reporteBase.setEspecieMascota("Perro");
        reporteBase.setIdUsuario(1L);

        MascotaDTO mascotaBase = new MascotaDTO();

        ReporteDTO candidato = new ReporteDTO();
        candidato.setIdReporte(2L);
        candidato.setIdMascota(20L);
        candidato.setIdUsuario(2L);

        MascotaDTO mascotaCandidato = new MascotaDTO();

        when(mascotasClient.obtenerReportePorId(1L)).thenReturn(reporteBase);
        when(mascotasClient.obtenerMascotaPorId(10L)).thenReturn(mascotaBase);
        when(geoClient.obtenerUbicacionesCercanas(100L, 6)).thenReturn(List.of(100L, 101L));

        when(mascotasClient.buscarReportesCandidatos(any(FiltroBusquedaMasivaDTO.class)))
                .thenReturn(List.of(candidato));
        when(mascotasClient.obtenerMascotaPorId(20L)).thenReturn(mascotaCandidato);

        when(motorSimilitud.evaluar(mascotaBase, mascotaCandidato)).thenReturn(85.0);
        when(repository.existsByReportePerdidaIdAndReporteHallazgoId(1L, 2L)).thenReturn(false);

        coincidenciaService.procesarReporte(1L);

        ArgumentCaptor<Coincidencia> coincidenciaCaptor = ArgumentCaptor.forClass(Coincidencia.class);
        verify(repository).save(coincidenciaCaptor.capture());

        Coincidencia guardada = coincidenciaCaptor.getValue();
        assertEquals(1L, guardada.getReportePerdidaId());
        assertEquals(2L, guardada.getReporteHallazgoId());
        assertEquals(85.0, guardada.getPorcentajeSimilitud());
        assertEquals(EstadoCoincidencia.PENDIENTE, guardada.getEstado());

        verify(rabbitMQProducer).enviarNotificacion(
                eq(1L), any(), any(), eq("COINCIDENCIA"), eq("/reportes/2"));
        verify(rabbitMQProducer).enviarNotificacion(
                eq(2L), any(), any(), eq("COINCIDENCIA"), eq("/reportes/1"));
    }

    @Test
    void fallbackProcesarReporte_loggingNoLanzaExcepcion() {
        coincidenciaService.fallbackProcesarReporte(1L, new RuntimeException("Error"));
    }
}
