package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.client.CoincidenciaClient;
import com.sanosysalvos.mascotas.client.UbicacionClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MascotasIntegrationService {

    private final UbicacionClient ubicacionClient;
    private final CoincidenciaClient coincidenciaClient;

    @CircuitBreaker(name = "geoCB", fallbackMethod = "fallbackObtenerUbicacion")
    public ResponseEntity<Object> obtenerUbicacion(Long idUbicacion) {
        return ubicacionClient.obtenerUbicacion(idUbicacion);
    }

    public ResponseEntity<Object> fallbackObtenerUbicacion(Long idUbicacion, Throwable t) {
        log.error("Fallback activado para validar ubicación en ms-geo (id: {}): {}", idUbicacion, t.getMessage());
        // Devolvemos 200 OK con un map vacío para que la validación asuma que la ubicación es válida temporalmente
        return ResponseEntity.ok(new HashMap<>());
    }

    @CircuitBreaker(name = "coincidenciaCB", fallbackMethod = "fallbackProcesarReporteTrigger")
    public ResponseEntity<Object> procesarReporteTrigger(Long reporteId) {
        return coincidenciaClient.procesarReporteTrigger(reporteId);
    }

    public ResponseEntity<Object> fallbackProcesarReporteTrigger(Long reporteId, Throwable t) {
        log.error("Fallback activado para procesarReporteTrigger (reporteId: {}): {}", reporteId, t.getMessage());
        // Devolvemos 200 OK silencioso para no interrumpir el flujo principal
        return ResponseEntity.ok().build();
    }
}
