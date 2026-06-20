package com.sanosysalvos.coincidencias.integration.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-geo", url = "${app.feign.ms-geo.url}")
public interface GeoClient {

    @GetMapping("/geo/v1/ubicaciones/{id}/cercanas")
    List<Long> obtenerUbicacionesCercanas(@PathVariable("id") Long id, @RequestParam("radio") Integer radio);
}

