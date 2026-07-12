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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoincidenciaServiceImplTest {

    @Mock
    private MascotasClient mascotasClient;

    @Mock
    private GeoClient geoClient;

    @Mock
    private MotorSimilitud motorSimilitud;

    @Mock
    private CoincidenciaRepository repository;

    @InjectMocks
    private CoincidenciaServiceImpl service;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(service, "radioBusqueda", 6);
    }

    @Test
    public void testProcesarReporteNoUbicacion() {
        ReporteDTO base = new ReporteDTO();
        base.setIdReporte(1L);
        base.setIdMascota(2L);
        base.setIdUbicacionReporte(null);
        when(mascotasClient.obtenerReportePorId(1L)).thenReturn(base);

        service.procesarReporte(1L);

        verify(geoClient, never()).obtenerUbicacionesCercanas(any(), anyInt());
    }

    @Test
    public void testProcesarReporteNoMascota() {
        ReporteDTO base = new ReporteDTO();
        base.setIdReporte(1L);
        base.setIdMascota(null);
        base.setIdUbicacionReporte(3L);
        when(mascotasClient.obtenerReportePorId(1L)).thenReturn(base);

        service.procesarReporte(1L);

        verify(mascotasClient, never()).obtenerMascotaPorId(any());
    }

    @Test
    public void testProcesarReporteSuccessfulMatch() {
        ReporteDTO baseReport = new ReporteDTO();
        baseReport.setIdReporte(1L);
        baseReport.setIdMascota(2L);
        baseReport.setIdUbicacionReporte(3L);
        baseReport.setTipoReporte("Mascota Perdida");
        baseReport.setEspecieMascota("Perro");
        
        MascotaDTO baseMascota = MascotaDTO.builder().id(2L).nombre("Firulais").build();
        
        when(mascotasClient.obtenerReportePorId(1L)).thenReturn(baseReport);
        when(mascotasClient.obtenerMascotaPorId(2L)).thenReturn(baseMascota);
        when(geoClient.obtenerUbicacionesCercanas(3L, 6)).thenReturn(Arrays.asList(10L, 11L));
        
        ReporteDTO candidatoReport = new ReporteDTO();
        candidatoReport.setIdReporte(100L);
        candidatoReport.setIdMascota(102L);
        candidatoReport.setIdUbicacionReporte(10L);
        candidatoReport.setTipoReporte("Mascota Encontrada / Avistamiento");
        candidatoReport.setEspecieMascota("Perro");
        
        MascotaDTO candidatoMascota = MascotaDTO.builder().id(102L).nombre("Firulais clone").build();
        
        when(mascotasClient.buscarReportesCandidatos(any(FiltroBusquedaMasivaDTO.class)))
                .thenReturn(Collections.singletonList(candidatoReport));
        when(mascotasClient.obtenerMascotaPorId(102L)).thenReturn(candidatoMascota);
        
        when(motorSimilitud.evaluar(baseMascota, candidatoMascota)).thenReturn(85.0);
        when(repository.existsByReportePerdidaIdAndReporteHallazgoId(1L, 100L)).thenReturn(false);

        service.procesarReporte(1L);

        verify(repository, times(1)).save(any(Coincidencia.class));
    }
}
