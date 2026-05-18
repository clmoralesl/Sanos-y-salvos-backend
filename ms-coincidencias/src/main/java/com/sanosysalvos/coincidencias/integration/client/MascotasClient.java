package com.sanosysalvos.coincidencias.integration.client;

import com.sanosysalvos.coincidencias.integration.dto.FiltroBusquedaMasivaDTO;
import com.sanosysalvos.coincidencias.integration.dto.ReporteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-mascotas", url = "${app.feign.ms-mascotas.url}")
public interface MascotasClient {

    @GetMapping("/api/v1/reportes/{id}")
    ReporteDTO obtenerReportePorId(@PathVariable("id") Long id);

    @PostMapping("/api/v1/reportes/busqueda-masiva")
    List<ReporteDTO> buscarReportesCandidatos(@RequestBody FiltroBusquedaMasivaDTO filtro);
}

