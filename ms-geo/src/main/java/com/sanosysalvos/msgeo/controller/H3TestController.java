package com.sanosysalvos.msgeo.controller;

import com.uber.h3core.H3Core;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/h3-test")
public class H3TestController {

    @GetMapping("/indice")
    public ResponseEntity<Map<String, Object>> obtenerIndice(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(defaultValue = "9") int resolucion) {

        try {
            H3Core h3 = H3Core.newInstance();
            String indiceHexagonal = h3.latLngToCellAddress(latitud, longitud, resolucion);
            
            Map<String, Object> response = new HashMap<>();
            response.put("latitud", latitud);
            response.put("longitud", longitud);
            response.put("resolucion", resolucion);
            response.put("indiceHexagonal", indiceHexagonal);

            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
