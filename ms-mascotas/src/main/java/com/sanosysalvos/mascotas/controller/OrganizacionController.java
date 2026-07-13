package com.sanosysalvos.mascotas.controller;

import com.sanosysalvos.mascotas.dto.OrganizacionRequestDTO;
import com.sanosysalvos.mascotas.dto.OrganizacionResponseDTO;
import com.sanosysalvos.mascotas.service.OrganizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mascotas/v1/organizaciones")
@RequiredArgsConstructor
public class OrganizacionController {

    private final OrganizacionService organizacionService;

    @PostMapping
    public ResponseEntity<OrganizacionResponseDTO> crearOrganizacion(@RequestBody OrganizacionRequestDTO request) {
        OrganizacionResponseDTO organizacion = organizacionService.crearOrganizacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(organizacion);
    }

    @GetMapping
    public ResponseEntity<List<OrganizacionResponseDTO>> listarOrganizaciones() {
        List<OrganizacionResponseDTO> organizaciones = organizacionService.obtenerTodas();
        return ResponseEntity.ok(organizaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizacionResponseDTO> obtenerOrganizacion(@PathVariable Long id) {
        OrganizacionResponseDTO organizacion = organizacionService.obtenerOrganizacionPorId(id);
        return ResponseEntity.ok(organizacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizacionResponseDTO> actualizarOrganizacion(@PathVariable Long id, @RequestBody OrganizacionRequestDTO request) {
        OrganizacionResponseDTO organizacion = organizacionService.actualizarOrganizacion(id, request);
        return ResponseEntity.ok(organizacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrganizacion(@PathVariable Long id) {
        organizacionService.eliminarOrganizacion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrganizacionResponseDTO> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        OrganizacionResponseDTO organizacion = organizacionService.actualizarEstado(id, estado);
        return ResponseEntity.ok(organizacion);
    }
}
