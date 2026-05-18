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

    
    private String nombreOrganizacion;
    private String descripcionTipoCuenta;
}

