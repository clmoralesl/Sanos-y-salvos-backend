package com.sanosysalvos.mascotas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    
    @NotBlank(message = "El identificador de Auth0 es obligatorio")
    private String auth0Id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un correo electrónico válido")
    private String email;

    private String telefono;

    
    private Long idOrganizacion;
    private Long idTipoCuenta;
}

