package com.sanosysalvos.mascotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequestDTO {
    @NotNull(message = "El ID del tipo de reporte es obligatorio")
    private Long idTipoReporte;

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long idMascota;

    @NotNull(message = "El ID de la ubicación es obligatorio")
    private Long idUbicacionReporte;

    @NotNull(message = "La fecha del incidente es obligatoria")
    private LocalDateTime fechaIncidente;
}
