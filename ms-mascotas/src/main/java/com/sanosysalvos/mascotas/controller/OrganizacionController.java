package com.sanosysalvos.mascotas.controller;

import com.sanosysalvos.mascotas.dto.OrganizacionRequestDTO;
import com.sanosysalvos.mascotas.dto.OrganizacionResponseDTO;
import com.sanosysalvos.mascotas.service.OrganizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/organizaciones")
@RequiredArgsConstructor
public class OrganizacionController {

    private final OrganizacionService organizacionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearOrganizacion(@RequestBody OrganizacionRequestDTO request) {
        OrganizacionResponseDTO organizacion = organizacionService.crearOrganizacion(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Organización creada exitosamente");
        response.put("data", organizacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarOrganizaciones() {
        List<OrganizacionResponseDTO> organizaciones = organizacionService.obtenerTodas();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Organizaciones recuperadas exitosamente");
        response.put("data", organizaciones);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerOrganizacion(@PathVariable Long id) {
        OrganizacionResponseDTO organizacion = organizacionService.obtenerOrganizacionPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Organización recuperada exitosamente");
        response.put("data", organizacion);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarOrganizacion(@PathVariable Long id, @RequestBody OrganizacionRequestDTO request) {
        OrganizacionResponseDTO organizacion = organizacionService.actualizarOrganizacion(id, request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Organización actualizada exitosamente");
        response.put("data", organizacion);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarOrganizacion(@PathVariable Long id) {
        organizacionService.eliminarOrganizacion(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Organización eliminada exitosamente");
        return ResponseEntity.ok(response);
    }
}
