package com.sanosysalvos.mascotas.controller;

import com.sanosysalvos.mascotas.dto.CatalogoResponseDTO;
import com.sanosysalvos.mascotas.service.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogos")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/razas")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerRazas() {
        return ResponseEntity.ok(catalogoService.obtenerRazas());
    }

    @GetMapping("/tamanios")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerTamanios() {
        return ResponseEntity.ok(catalogoService.obtenerTamanios());
    }

    @GetMapping("/caracteristicas")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerCaracteristicas() {
        return ResponseEntity.ok(catalogoService.obtenerCaracteristicas());
    }

    @GetMapping("/tipos-reporte")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerTiposReporte() {
        return ResponseEntity.ok(catalogoService.obtenerTiposReporte());
    }

    @GetMapping("/tipos-cuenta")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerTiposCuenta() {
        return ResponseEntity.ok(catalogoService.obtenerTiposCuenta());
    }
}
