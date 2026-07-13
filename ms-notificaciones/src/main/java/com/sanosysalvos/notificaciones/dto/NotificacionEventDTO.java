package com.sanosysalvos.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionEventDTO {
    private Long idUsuarioDestino;
    private String titulo;
    private String mensaje;
    private String tipo;
}
