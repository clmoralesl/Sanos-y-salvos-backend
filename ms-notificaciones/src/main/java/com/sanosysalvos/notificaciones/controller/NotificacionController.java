package com.sanosysalvos.notificaciones.controller;

import com.sanosysalvos.notificaciones.entity.Notificacion;
import com.sanosysalvos.notificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones/v1/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Notificacion>> obtenerPorUsuario(@PathVariable Long id, @RequestParam(required = false, defaultValue = "false") boolean soloNoLeidas) {
        if (soloNoLeidas) {
            return ResponseEntity.ok(notificacionService.obtenerNoLeidasPorUsuario(id));
        }
        return ResponseEntity.ok(notificacionService.obtenerPorUsuario(id));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }
}
