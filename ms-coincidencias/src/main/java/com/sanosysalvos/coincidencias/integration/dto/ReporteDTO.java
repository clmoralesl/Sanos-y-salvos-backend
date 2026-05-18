package com.sanosysalvos.coincidencias.integration.dto;

import lombok.Data;

@Data
public class ReporteDTO {
    private Long id;
    private String tipoReporte; 
    private Long ubicacionId;
    private MascotaDTO mascota;
}

