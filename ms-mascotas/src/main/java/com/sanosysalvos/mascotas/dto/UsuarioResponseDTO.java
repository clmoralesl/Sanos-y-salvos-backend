package com.sanosysalvos.mascotas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long idUsuario;
    private String auth0Id;
    private String nombre;
    private String email;
    private String telefono;
    
    // Retornamos nombres descriptivos en vez de IDs para mejor UX en el Fronend
    private String nombreOrganizacion;
    private String descripcionTipoCuenta;
}
