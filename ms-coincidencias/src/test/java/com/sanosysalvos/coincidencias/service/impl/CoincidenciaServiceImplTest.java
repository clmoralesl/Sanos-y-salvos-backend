package com.sanosysalvos.coincidencias.service.impl;

import com.sanosysalvos.coincidencias.business.MotorSimilitud;
import com.sanosysalvos.coincidencias.domain.entity.Coincidencia;
import com.sanosysalvos.coincidencias.integration.client.GeoClient;
import com.sanosysalvos.coincidencias.integration.client.MascotasClient;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import com.sanosysalvos.coincidencias.integration.dto.ReporteDTO;
import com.sanosysalvos.coincidencias.repository.CoincidenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CoincidenciaServiceImplTest {

    private MascotasClient mascotasClient;
    private GeoClient geoClient;
    private MotorSimilitud motorSimilitud;
    private CoincidenciaRepository repository;
    private CoincidenciaServiceImpl service;

    @BeforeEach
    void setUp() {
        mascotasClient = mock(MascotasClient.class);
        geoClient = mock(GeoClient.class);
        motorSimilitud = mock(MotorSimilitud.class);
        repository = mock(CoincidenciaRepository.class);

        service = new CoincidenciaServiceImpl(
                mascotasClient,
                geoClient,
                motorSimilitud,
                repository
        );

        ReflectionTestUtils.setField(service, "radioBusqueda", 6);
    }

    @Test
    void debeGuardarCoincidenciaCuandoSimilitudSuperaUmbral() {

        ReporteDTO reporteBase = new ReporteDTO();
        reporteBase.setIdReporte(1L);
        reporteBase.setIdMascota(10L);
        reporteBase.setIdUbicacionReporte(100L);
        reporteBase.setTipoReporte("Mascota Perdida");
        reporteBase.setEspecieMascota("Perro");

        ReporteDTO candidato = new ReporteDTO();
        candidato.setIdReporte(2L);
        candidato.setIdMascota(20L);

        MascotaDTO mascotaBase = MascotaDTO.builder()
                .id(10L)
                .build();

        MascotaDTO mascotaCandidato = MascotaDTO.builder()
                .id(20L)
                .build();

        when(mascotasClient.obtenerReportePorId(1L))
                .thenReturn(reporteBase);

        when(mascotasClient.obtenerMascotaPorId(10L))
                .thenReturn(mascotaBase);

        when(geoClient.obtenerUbicacionesCercanas(100L, 6))
                .thenReturn(List.of(100L, 101L));

        when(mascotasClient.buscarReportesCandidatos(any()))
                .thenReturn(List.of(candidato));

        when(mascotasClient.obtenerMascotaPorId(20L))
                .thenReturn(mascotaCandidato);

        when(motorSimilitud.evaluar(mascotaBase, mascotaCandidato))
                .thenReturn(85.0);

        when(repository.existsByReportePerdidaIdAndReporteHallazgoId(1L, 2L))
                .thenReturn(false);

        service.procesarReporte(1L);

        ArgumentCaptor<Coincidencia> captor =
                ArgumentCaptor.forClass(Coincidencia.class);

        verify(repository).save(captor.capture());

        Coincidencia guardada = captor.getValue();

        assertEquals(1L, guardada.getReportePerdidaId());
        assertEquals(2L, guardada.getReporteHallazgoId());
        assertEquals(85.0, guardada.getPorcentajeSimilitud());
    }

    @Test
    void noDebeGuardarCoincidenciaCuandoSimilitudEsMenorAlUmbral() {

        ReporteDTO reporteBase = new ReporteDTO();
        reporteBase.setIdReporte(1L);
        reporteBase.setIdMascota(10L);
        reporteBase.setIdUbicacionReporte(100L);
        reporteBase.setTipoReporte("Mascota Perdida");
        reporteBase.setEspecieMascota("Perro");

        ReporteDTO candidato = new ReporteDTO();
        candidato.setIdReporte(2L);
        candidato.setIdMascota(20L);

        MascotaDTO mascotaBase = MascotaDTO.builder()
                .id(10L)
                .build();

        MascotaDTO mascotaCandidato = MascotaDTO.builder()
                .id(20L)
                .build();

        when(mascotasClient.obtenerReportePorId(1L))
                .thenReturn(reporteBase);

        when(mascotasClient.obtenerMascotaPorId(10L))
                .thenReturn(mascotaBase);

        when(geoClient.obtenerUbicacionesCercanas(100L, 6))
                .thenReturn(List.of(100L, 101L));

        when(mascotasClient.buscarReportesCandidatos(any()))
                .thenReturn(List.of(candidato));

        when(mascotasClient.obtenerMascotaPorId(20L))
                .thenReturn(mascotaCandidato);

        when(motorSimilitud.evaluar(mascotaBase, mascotaCandidato))
                .thenReturn(25.0);

        service.procesarReporte(1L);

        verify(repository, never()).save(any());
    }

    @Test
    void noDebeProcesarCuandoReporteNoTieneUbicacion() {

        ReporteDTO reporteBase = new ReporteDTO();
        reporteBase.setIdReporte(1L);
        reporteBase.setIdMascota(10L);

        when(mascotasClient.obtenerReportePorId(1L))
                .thenReturn(reporteBase);

        service.procesarReporte(1L);

        verifyNoInteractions(geoClient);
        verify(repository, never()).save(any());
    }
@Test
void noDebeGuardarCoincidenciaSiYaExiste() {

    ReporteDTO reporteBase = new ReporteDTO();
    reporteBase.setIdReporte(1L);
    reporteBase.setIdMascota(10L);
    reporteBase.setIdUbicacionReporte(100L);
    reporteBase.setTipoReporte("Mascota Perdida");
    reporteBase.setEspecieMascota("Perro");

    ReporteDTO candidato = new ReporteDTO();
    candidato.setIdReporte(2L);
    candidato.setIdMascota(20L);

    MascotaDTO mascotaBase = MascotaDTO.builder().id(10L).build();
    MascotaDTO mascotaCandidato = MascotaDTO.builder().id(20L).build();

    when(mascotasClient.obtenerReportePorId(1L)).thenReturn(reporteBase);
    when(mascotasClient.obtenerMascotaPorId(10L)).thenReturn(mascotaBase);
    when(geoClient.obtenerUbicacionesCercanas(100L, 6)).thenReturn(List.of(100L));
    when(mascotasClient.buscarReportesCandidatos(any())).thenReturn(List.of(candidato));
    when(mascotasClient.obtenerMascotaPorId(20L)).thenReturn(mascotaCandidato);
    when(motorSimilitud.evaluar(mascotaBase, mascotaCandidato)).thenReturn(90.0);

    when(repository.existsByReportePerdidaIdAndReporteHallazgoId(1L, 2L))
            .thenReturn(true);

    service.procesarReporte(1L);

    verify(repository, never()).save(any());
} 
@Test
void noDebeProcesarCuandoMascotaBaseNoExiste() {

    ReporteDTO reporteBase = new ReporteDTO();
    reporteBase.setIdReporte(1L);
    reporteBase.setIdMascota(10L);
    reporteBase.setIdUbicacionReporte(100L);

    when(mascotasClient.obtenerReportePorId(1L))
            .thenReturn(reporteBase);

    when(mascotasClient.obtenerMascotaPorId(10L))
            .thenReturn(null);

    service.procesarReporte(1L);

    verifyNoInteractions(geoClient);
    verify(repository, never()).save(any());
}
   @Test
void noDebeProcesarCuandoReporteNoExiste() {

    when(mascotasClient.obtenerReportePorId(1L))
            .thenReturn(null);

    service.procesarReporte(1L);

    verifyNoInteractions(geoClient);
    verify(repository, never()).save(any());
}

@Test
void noDebeProcesarCuandoReporteNoTieneMascota() {

    ReporteDTO reporteBase = new ReporteDTO();
    reporteBase.setIdReporte(1L);
    reporteBase.setIdUbicacionReporte(100L);
    reporteBase.setIdMascota(null);

    when(mascotasClient.obtenerReportePorId(1L))
            .thenReturn(reporteBase);

    service.procesarReporte(1L);

    verifyNoInteractions(geoClient);
    verify(repository, never()).save(any());
}

@Test
void noDebeGuardarCuandoNoHayUbicacionesCercanas() {

    ReporteDTO reporteBase = new ReporteDTO();
    reporteBase.setIdReporte(1L);
    reporteBase.setIdMascota(10L);
    reporteBase.setIdUbicacionReporte(100L);

    MascotaDTO mascotaBase = MascotaDTO.builder()
            .id(10L)
            .build();

    when(mascotasClient.obtenerReportePorId(1L))
            .thenReturn(reporteBase);

    when(mascotasClient.obtenerMascotaPorId(10L))
            .thenReturn(mascotaBase);

    when(geoClient.obtenerUbicacionesCercanas(100L, 6))
            .thenReturn(List.of());

    service.procesarReporte(1L);

    verify(mascotasClient, never()).buscarReportesCandidatos(any());
    verify(repository, never()).save(any());
}
}
