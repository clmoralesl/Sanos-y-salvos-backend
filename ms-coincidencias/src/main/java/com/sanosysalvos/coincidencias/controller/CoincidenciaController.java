package com.sanosysalvos.coincidencias.controller;

import com.sanosysalvos.coincidencias.domain.entity.Coincidencia;
import com.sanosysalvos.coincidencias.repository.CoincidenciaRepository;
import com.sanosysalvos.coincidencias.service.CoincidenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/coincidencias")
@RequiredArgsConstructor
public class CoincidenciaController {

    private final CoincidenciaService coincidenciaService;
    private final CoincidenciaRepository coincidenciaRepository;

    /**
     * Endpoint asíncrono/trigger.
     * Es invocado generalmente por ms-mascotas cuando se crea un nuevo reporte.
     * Retorna "Aceptado" indicando que el motor procesará en background.
     */
    @PostMapping("/trigger/{reporteId}")
    public ResponseEntity<Map<String, Object>> procesarReporteTrigger(@PathVariable Long reporteId) {
        // En una arquitectura más madura esto idealmente sería mediante un broker (Kafka/RabbitMQ).
        // Por ahora, al ser un enfoque Híbrido, respondemos rápido y delegamos el proceso.
        // Aunque aquí hacemos llamada directa, en un escenario real podríamos usar @Async o un Hilo.
        // Para fines de simplicidad lo ejecutamos directamente, pero se podría agendar.
        
        coincidenciaService.procesarReporte(reporteId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Procesamiento de coincidencias iniciado para el reporte " + reporteId);
        return ResponseEntity.accepted().body(response);
    }

    /**
     * Obtiene las coincidencias que existen para un reporte de PERDIDA.
     */
    @GetMapping("/perdida/{reporteId}")
    public ResponseEntity<List<Coincidencia>> obtenerPorPerdida(@PathVariable Long reporteId) {
        List<Coincidencia> coincidencias = coincidenciaRepository.findByReportePerdidaId(reporteId);
        return ResponseEntity.ok(coincidencias);
    }

    /**
     * Obtiene las coincidencias que existen para un reporte de HALLAZGO.
     */
    @GetMapping("/hallazgo/{reporteId}")
    public ResponseEntity<List<Coincidencia>> obtenerPorHallazgo(@PathVariable Long reporteId) {
        List<Coincidencia> coincidencias = coincidenciaRepository.findByReporteHallazgoId(reporteId);
        return ResponseEntity.ok(coincidencias);
    }
}
