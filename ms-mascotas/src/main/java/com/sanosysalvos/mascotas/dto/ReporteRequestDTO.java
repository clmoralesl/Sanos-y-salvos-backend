package com.sanosysalvos.mascotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequestDTO {
    @NotNull(message = "El ID del tipo de reporte es obligatorio")
    private Long idTipoReporte;

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long idMascota;

    private Long idUbicacionReporte; // Optional por ahora
}
