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
@RequestMapping("/coincidencias/v1/coincidencias")
@RequiredArgsConstructor
public class CoincidenciaController {

    private final CoincidenciaService coincidenciaService;
    private final CoincidenciaRepository coincidenciaRepository;


    @PostMapping("/trigger/{reporteId}")
    public ResponseEntity<Map<String, Object>> procesarReporteTrigger(@PathVariable Long reporteId) {

        coincidenciaService.procesarReporte(reporteId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Procesamiento de coincidencias iniciado para el reporte " + reporteId);
        return ResponseEntity.accepted().body(response);
    }


    @GetMapping("/perdida/{reporteId}")
    public ResponseEntity<List<Coincidencia>> obtenerPorPerdida(@PathVariable Long reporteId) {
        List<Coincidencia> coincidencias = coincidenciaRepository.findByReportePerdidaId(reporteId);
        return ResponseEntity.ok(coincidencias);
    }


    @GetMapping("/hallazgo/{reporteId}")
    public ResponseEntity<List<Coincidencia>> obtenerPorHallazgo(@PathVariable Long reporteId) {
        List<Coincidencia> coincidencias = coincidenciaRepository.findByReporteHallazgoId(reporteId);
        return ResponseEntity.ok(coincidencias);
    }
}

