package com.sanosysalvos.mascotas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    
    @Column(name = "auth0_id", unique = true, length = 100)
    private String auth0Id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_organizacion")
    private Organizacion organizacion;

    @ManyToOne
    @JoinColumn(name = "id_tipo_cuenta")
    private TipoCuenta tipoCuenta;
}

