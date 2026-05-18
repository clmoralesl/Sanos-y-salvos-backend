package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.dto.ReporteDetalleDTO;
import com.sanosysalvos.bff.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bff/v1/reportes")
@RequiredArgsConstructor
public class BffReporteController {

    private final OrchestrationService orchestrationService;

    @GetMapping("/{id}/detalle")
    public ResponseEntity<ReporteDetalleDTO> obtenerDetalleReporte(
            @PathVariable Long id,
            @RequestHeader("X-Auth0-Id") String auth0Id) {

        return ResponseEntity.ok(orchestrationService.obtenerDetalleCompleto(id, auth0Id));
    }
}

