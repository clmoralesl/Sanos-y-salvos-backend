package com.sanosysalvos.mascotas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaResponseDTO {

    private Long idMascota;
    private String nombreMascota;
    private String descripcion;
    private String nombreRaza;
    private String especieRaza;
    private String descripcionTamanio;
    private String nombreDueno;
    private List<String> caracteristicas;
    private List<String> urlsFotografias;

}

