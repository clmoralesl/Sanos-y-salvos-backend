package com.sanosysalvos.mascotas.controller;

import com.sanosysalvos.mascotas.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mascotas/v1/storage")
@RequiredArgsConstructor
public class StorageController {

    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public ResponseEntity<Map<String, String>> obtenerUrlFirmada(
            @RequestParam("tipo") String tipo,
            @RequestParam("contentType") String contentType) {
        
        Map<String, String> response = s3Service.generarUrlFirmada(tipo, contentType);
        return ResponseEntity.ok(response);
    }
}
