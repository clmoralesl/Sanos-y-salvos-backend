package com.sanosysalvos.msgeo.dto;

import lombok.Data;

@Data
public class UbicacionRequestDTO {
    private Double latitud;
    private Double longitud;
    private Long idComuna;
    private String direccionEspecifica;
}
