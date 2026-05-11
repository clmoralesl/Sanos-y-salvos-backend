package com.sanosysalvos.msgeo.controller;

import com.sanosysalvos.msgeo.model.Comuna;
import com.sanosysalvos.msgeo.model.Region;
import com.sanosysalvos.msgeo.service.CatalogoGeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogos-geo")
@RequiredArgsConstructor
public class CatalogoGeoController {

    private final CatalogoGeoService catalogoGeoService;

    @GetMapping("/regiones")
    public ResponseEntity<List<Region>> obtenerRegiones() {
        return ResponseEntity.ok(catalogoGeoService.obtenerRegiones());
    }

    @GetMapping("/regiones/{idRegion}/comunas")
    public ResponseEntity<List<Comuna>> obtenerComunasPorRegion(@PathVariable Long idRegion) {
        return ResponseEntity.ok(catalogoGeoService.obtenerComunasPorRegion(idRegion));
    }
}
