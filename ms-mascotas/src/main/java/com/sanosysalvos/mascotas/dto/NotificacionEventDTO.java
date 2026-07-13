package com.sanosysalvos.mascotas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionEventDTO {
    private Long idUsuarioDestino;
    private String titulo;
    private String mensaje;
    private String tipo;
}
