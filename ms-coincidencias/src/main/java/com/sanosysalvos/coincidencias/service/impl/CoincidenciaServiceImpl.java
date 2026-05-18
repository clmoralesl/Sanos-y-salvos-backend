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

    private static final double UMBRAL_SIMILITUD = 60.0;
    private static final int RADIO_BUSQUEDA_DEFAULT = 1;
    @Override
    @CircuitBreaker(name = "coincidenciasCB", fallbackMethod = "fallbackProcesarReporte")
    public void procesarReporte(Long reporteId) {
        log.info("Iniciando procesamiento de coincidencias para el reporte ID: {}", reporteId);

        
        ReporteDTO reporteBase = mascotasClient.obtenerReportePorId(reporteId);
        if (reporteBase == null || reporteBase.getUbicacionId() == null) {
            log.warn("El reporte ID {} no existe o no tiene ubicacion asociada.", reporteId);
            return;
        }

        
        List<Long> ubicacionesCercanas = geoClient.obtenerUbicacionesCercanas(
                reporteBase.getUbicacionId(), RADIO_BUSQUEDA_DEFAULT);

        if (ubicacionesCercanas == null || ubicacionesCercanas.isEmpty()) {
            log.info("No se encontraron ubicaciones cercanas para el reporte {}", reporteId);
            return;
        }

        
        String tipoBuscado = "PERDIDA".equalsIgnoreCase(reporteBase.getTipoReporte()) ? "HALLAZGO" : "PERDIDA";

        
        FiltroBusquedaMasivaDTO filtro = FiltroBusquedaMasivaDTO.builder()
                .tipoReporteBuscado(tipoBuscado)
                .especie(reporteBase.getMascota().getEspecie())
                .ubicacionesIds(ubicacionesCercanas)
                .build();

        List<ReporteDTO> candidatos = mascotasClient.buscarReportesCandidatos(filtro);

        
        for (ReporteDTO candidato : candidatos) {
            double similitud = motorSimilitud.evaluar(reporteBase.getMascota(), candidato.getMascota());
            log.debug("Similitud entre reporte {} y {} es de {}%", reporteId, candidato.getId(), similitud);

            
            if (similitud >= UMBRAL_SIMILITUD) {
                Long perdidaId = "PERDIDA".equalsIgnoreCase(reporteBase.getTipoReporte()) ? reporteBase.getId() : candidato.getId();
                Long hallazgoId = "HALLAZGO".equalsIgnoreCase(reporteBase.getTipoReporte()) ? reporteBase.getId() : candidato.getId();

                
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

