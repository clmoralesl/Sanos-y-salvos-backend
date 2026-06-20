package com.sanosysalvos.coincidencias.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MascotaDTO {

    @JsonProperty("idMascota")
    private Long id;

    @JsonProperty("nombreMascota")
    private String nombre;

    @JsonProperty("especieRaza")
    private String especie;

    @JsonProperty("nombreRaza")
    private String raza;

    @JsonProperty("descripcionTamanio")
    private String tamanio;

    @JsonProperty("caracteristicas")
    private List<String> caracteristicas;
}
