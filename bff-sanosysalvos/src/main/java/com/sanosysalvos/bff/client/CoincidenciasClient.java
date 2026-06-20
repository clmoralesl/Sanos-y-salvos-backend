package com.sanosysalvos.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "ms-coincidencias", url = "${clients.ms-coincidencias.url}")
public interface CoincidenciasClient {

    @GetMapping("/coincidencias/v1/coincidencias/perdida/{reporteId}")
    List<Map<String, Object>> obtenerPorPerdida(@PathVariable("reporteId") Long reporteId);

    @GetMapping("/coincidencias/v1/coincidencias/hallazgo/{reporteId}")
    List<Map<String, Object>> obtenerPorHallazgo(@PathVariable("reporteId") Long reporteId);
}
