package com.sanosysalvos.mascotas.dto;

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
    private String tipoReporteBuscado; 
    private String especie;
    private List<Long> ubicacionesIds; 
}
