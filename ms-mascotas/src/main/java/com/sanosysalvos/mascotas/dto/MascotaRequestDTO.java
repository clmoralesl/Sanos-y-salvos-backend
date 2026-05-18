package com.sanosysalvos.mascotas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaRequestDTO {

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    private String nombreMascota;

    private String descripcion;

    @NotNull(message = "El ID de la raza es obligatorio")
    private Long idRaza;

    @NotNull(message = "El ID del tamaño es obligatorio")
    private Long idTamanio;

    private List<Long> idsCaracteristicas;

    private List<String> urlsFotografias;
}

