package com.sanosysalvos.mascotas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponseDTO {
    private Long idReporte;
    private LocalDateTime fechaReporte;
    private Long idUbicacionReporte;
    private String tipoReporte;
    private String estadoReporte;
    private Long idUsuario;
    private String nombreUsuario;
    private Long idMascota;
    private String nombreMascota;
}
