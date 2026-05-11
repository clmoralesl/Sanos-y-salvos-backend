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

    // Auth0 mandará este ID al Gateway, y el Gateway (o el controlador) lo incluirá aquí
    @NotBlank(message = "El identificador de Auth0 es obligatorio")
    private String auth0Id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un correo electrónico válido")
    private String email;

    private String telefono;
    
    // Estos pueden venir nulos si se registra por primera vez como usuario común
    private Long idOrganizacion;
    private Long idTipoCuenta;
}
