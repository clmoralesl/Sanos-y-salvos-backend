package com.sanosysalvos.mascotas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizacionRequestDTO {
    private String nombreOrganizacion;
    private String direccion;
    private String telefono;
    private String email;
    private String rut;
    private String rutRepresentante;
}
