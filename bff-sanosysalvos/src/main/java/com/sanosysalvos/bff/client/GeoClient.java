package com.sanosysalvos.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "ms-geo", url = "${clients.ms-geo.url}")
public interface GeoClient {

    @GetMapping("/geo/v1/ubicaciones/{id}")
    Map<String, Object> obtenerUbicacion(@PathVariable("id") Long id);
}

