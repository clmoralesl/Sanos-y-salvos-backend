package com.sanosysalvos.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Map;

@FeignClient(name = "ms-mascotas", url = "${clients.ms-mascotas.url}")
public interface MascotasClient {

    @GetMapping("/mascotas/v1/reportes/{id}")
    Map<String, Object> obtenerReporte(@PathVariable("id") Long id, @RequestHeader("X-Auth0-Id") String auth0Id);

    @GetMapping("/mascotas/v1/mascotas/{id}")
    Map<String, Object> obtenerMascota(@PathVariable("id") Long id);

    @GetMapping("/mascotas/v1/usuarios/{id}")
    Map<String, Object> obtenerUsuario(@PathVariable("id") Long id);
}

