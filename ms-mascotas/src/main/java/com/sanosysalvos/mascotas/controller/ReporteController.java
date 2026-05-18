package com.sanosysalvos.mascotas.controller;

import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mascotas/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearReporte(@Valid @RequestBody ReporteRequestDTO request,
                                                           @RequestHeader("X-Auth0-Id") String auth0Id) {
        ReporteResponseDTO data = reporteService.crearReporte(request, auth0Id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reporte creado exitosamente");
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(reporteService.obtenerTodosLosReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.obtenerReportePorId(id));
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<Map<String, Object>> cerrarReporte(@PathVariable Long id, @RequestHeader("X-Auth0-Id") String auth0Id) {
        ReporteResponseDTO data = reporteService.cerrarReporte(id, auth0Id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reporte " + id + " cerrado correctamente");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarReporte(@PathVariable Long id, @Valid @RequestBody ReporteRequestDTO request, @RequestHeader("X-Auth0-Id") String auth0Id) {
        ReporteResponseDTO data = reporteService.actualizarReporte(id, request, auth0Id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reporte " + id + " actualizado correctamente");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarReporte(@PathVariable Long id, @RequestHeader("X-Auth0-Id") String auth0Id) {
        reporteService.eliminarReporte(id, auth0Id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reporte " + id + " eliminado correctamente");
        return ResponseEntity.ok(response);
    }
}
