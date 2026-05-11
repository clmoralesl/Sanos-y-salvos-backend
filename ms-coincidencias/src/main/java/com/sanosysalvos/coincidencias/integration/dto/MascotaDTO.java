package com.sanosysalvos.coincidencias.integration.dto;

import lombok.Data;
import java.util.List;

@Data
public class MascotaDTO {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private String tamanio;
    private List<CaracteristicaDTO> caracteristicasFisicas;
}
