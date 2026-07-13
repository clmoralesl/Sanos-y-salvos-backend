package com.sanosysalvos.notificaciones.integration;

import com.sanosysalvos.notificaciones.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-mascotas", url = "${app.feign.ms-mascotas.url}")
public interface MascotasClient {

    @GetMapping("/mascotas/v1/usuarios/{id}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("id") Long id);
}
