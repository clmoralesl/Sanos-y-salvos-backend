package com.sanosysalvos.mascotas.controller;

import com.sanosysalvos.mascotas.dto.MascotaRequestDTO;
import com.sanosysalvos.mascotas.dto.MascotaResponseDTO;
import com.sanosysalvos.mascotas.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createMascota(@Valid @RequestBody MascotaRequestDTO request) {
        MascotaResponseDTO data = mascotaService.createMascota(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mascota guardada correctamente");
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> getMascotaById(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.getMascotaById(id));
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponseDTO>> getAllMascotas() {
        return ResponseEntity.ok(mascotaService.getAllMascotas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMascota(@PathVariable Long id, @Valid @RequestBody MascotaRequestDTO request) {
        MascotaResponseDTO data = mascotaService.updateMascota(id, request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mascota actualizada correctamente");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMascota(@PathVariable Long id) {
        mascotaService.deleteMascota(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mascota " + id + " eliminada correctamente");
        return ResponseEntity.ok(response);
    }
}
