package com.sanosysalvos.mascotas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-geo", url = "${clients.ms-geo.url}")
public interface UbicacionClient {

    @GetMapping("/geo/v1/ubicaciones/{id}")
    ResponseEntity<Object> obtenerUbicacion(@PathVariable("id") Long id);

}

