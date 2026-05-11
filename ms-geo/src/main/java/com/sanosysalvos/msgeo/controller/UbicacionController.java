package com.sanosysalvos.msgeo.controller;

import com.sanosysalvos.msgeo.dto.UbicacionRequestDTO;
import com.sanosysalvos.msgeo.service.UbicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ubicaciones")
@RequiredArgsConstructor
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearUbicacion(
            @RequestHeader("X-Auth0-Id") String auth0Id,
            @RequestBody UbicacionRequestDTO requestDTO) {
        
        Long idUbicacion = ubicacionService.crearUbicacion(requestDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ubicación guardada correctamente");
        response.put("idUbicacion", idUbicacion);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
