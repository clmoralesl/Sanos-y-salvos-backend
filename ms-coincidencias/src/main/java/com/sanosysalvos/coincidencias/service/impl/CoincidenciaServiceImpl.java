package com.sanosysalvos.coincidencias.service.impl;

import com.sanosysalvos.coincidencias.business.MotorSimilitud;
import com.sanosysalvos.coincidencias.domain.entity.Coincidencia;
import com.sanosysalvos.coincidencias.domain.enums.EstadoCoincidencia;
import com.sanosysalvos.coincidencias.integration.client.GeoClient;
import com.sanosysalvos.coincidencias.integration.client.MascotasClient;
import com.sanosysalvos.coincidencias.integration.dto.FiltroBusquedaMasivaDTO;
import com.sanosysalvos.coincidencias.integration.dto.ReporteDTO;
import com.sanosysalvos.coincidencias.repository.CoincidenciaRepository;
import com.sanosysalvos.coincidencias.service.CoincidenciaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoincidenciaServiceImpl implements CoincidenciaService {

    private final MascotasClient mascotasClient;
    private final GeoClient geoClient;
    private final MotorSimilitud motorSimilitud;
    private final CoincidenciaRepository repository;

    private static final double UMBRAL_SIMILITUD = 40.0;

    @org.springframework.beans.factory.annotation.Value("${coincidencias.radio-busqueda:6}")
    private int radioBusqueda;

    @Override
    @CircuitBreaker(name = "coincidenciasCB", fallbackMethod = "fallbackProcesarReporte")
    public void procesarReporte(Long reporteId) {
        log.info("Iniciando procesamiento de coincidencias para el reporte ID: {} (Radio H3 K={}, ~5km)", reporteId, radioBusqueda);

        ReporteDTO reporteBase = mascotasClient.obtenerReportePorId(reporteId);
        if (reporteBase == null || reporteBase.getIdUbicacionReporte() == null) {
            log.warn("El reporte ID {} no existe o no tiene ubicacion asociada.", reporteId);
            return;
        }

        if (reporteBase.getIdMascota() == null) {
            log.warn("El reporte ID {} no tiene mascota asociada.", reporteId);
            return;
        }

        com.sanosysalvos.coincidencias.integration.dto.MascotaDTO mascotaBase = mascotasClient.obtenerMascotaPorId(reporteBase.getIdMascota());
        if (mascotaBase == null) {
            log.warn("No se pudo obtener la mascota ID {} para el reporte base.", reporteBase.getIdMascota());
            return;
        }

        List<Long> ubicacionesCercanas = geoClient.obtenerUbicacionesCercanas(
                reporteBase.getIdUbicacionReporte(), radioBusqueda);

        if (ubicacionesCercanas == null || ubicacionesCercanas.isEmpty()) {
            log.info("No se encontraron ubicaciones cercanas para el reporte {}", reporteId);
            return;
        }

        boolean esPerdida = reporteBase.getTipoReporte() != null &&
                reporteBase.getTipoReporte().toLowerCase().contains("perdida");
        String tipoBuscado = esPerdida ? "Mascota Encontrada / Avistamiento" : "Mascota Perdida";

        FiltroBusquedaMasivaDTO filtro = FiltroBusquedaMasivaDTO.builder()
                .tipoReporteBuscado(tipoBuscado)
                .especie(reporteBase.getEspecieMascota())
                .ubicacionesIds(ubicacionesCercanas)
                .build();

        log.info("Llamando a ms-mascotas con filtro: {}", filtro);
        List<ReporteDTO> candidatos = mascotasClient.buscarReportesCandidatos(filtro);
        log.info("Candidatos encontrados: {}", candidatos.size());

        for (ReporteDTO candidato : candidatos) {
            if (candidato.getIdMascota() == null) {
                continue;
            }
            com.sanosysalvos.coincidencias.integration.dto.MascotaDTO mascotaCandidato = mascotasClient.obtenerMascotaPorId(candidato.getIdMascota());
            if (mascotaCandidato == null) {
                continue;
            }
            log.info("Evaluando candidato reporte ID: {} con mascota ID: {}", candidato.getIdReporte(), candidato.getIdMascota());
            double similitud = motorSimilitud.evaluar(mascotaBase, mascotaCandidato);
            log.info("Similitud entre reporte {} y {} es de {}% (Umbral: {})", reporteId, candidato.getIdReporte(), similitud, UMBRAL_SIMILITUD);

            if (similitud >= UMBRAL_SIMILITUD) {
                Long perdidaId = esPerdida ? reporteBase.getIdReporte() : candidato.getIdReporte();
                Long hallazgoId = esPerdida ? candidato.getIdReporte() : reporteBase.getIdReporte();

                if (!repository.existsByReportePerdidaIdAndReporteHallazgoId(perdidaId, hallazgoId)) {
                    Coincidencia nuevaCoincidencia = Coincidencia.builder()
                            .reportePerdidaId(perdidaId)
                            .reporteHallazgoId(hallazgoId)
                            .porcentajeSimilitud(similitud)
                            .estado(EstadoCoincidencia.PENDIENTE)
                            .build();

                    repository.save(nuevaCoincidencia);
                    log.info("Nueva coincidencia guardada: Perdida={}, Hallazgo={}, Similitud={}",
                            perdidaId, hallazgoId, similitud);
                }
            }
        }
    }

    public void fallbackProcesarReporte(Long reporteId, Throwable t) {
        log.error("CircuitBreaker ABIERTO o error en cascada. No se pudo procesar el reporte {}. Razón: {}",
                reporteId, t.getMessage());
    }
}

