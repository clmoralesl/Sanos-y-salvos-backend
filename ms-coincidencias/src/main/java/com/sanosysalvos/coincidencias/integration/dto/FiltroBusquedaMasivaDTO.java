package com.sanosysalvos.coincidencias.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiltroBusquedaMasivaDTO {
    private String tipoReporteBuscado; // Si recibimos PERDIDA, buscamos HALLAZGO y viceversa
    private String especie;
    private List<Long> ubicacionesIds; // IDs de ubicaciones cercanas
}
