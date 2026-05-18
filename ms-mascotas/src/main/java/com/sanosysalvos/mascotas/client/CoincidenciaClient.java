package com.sanosysalvos.mascotas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ms-coincidencias", url = "${clients.ms-coincidencias.url}")
public interface CoincidenciaClient {

    @PostMapping("/api/v1/coincidencias/trigger/{reporteId}")
    ResponseEntity<Object> procesarReporteTrigger(@PathVariable("reporteId") Long reporteId);

}

