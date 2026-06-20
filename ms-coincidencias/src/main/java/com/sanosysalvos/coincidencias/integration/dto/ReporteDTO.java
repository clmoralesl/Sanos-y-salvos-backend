package com.sanosysalvos.coincidencias.integration.dto;

import lombok.Data;

@Data
public class ReporteDTO {
    private Long idReporte;
    private String tipoReporte; 
    private Long idUbicacionReporte;
    private Long idMascota;
    private String nombreMascota;
    private String especieMascota; 
    
    
    public MascotaDTO getMascota() {
        return MascotaDTO.builder()
                .id(idMascota)
                .nombre(nombreMascota)
                .especie(especieMascota)
                .build();
    }
}

