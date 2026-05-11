package com.sanosysalvos.mascotas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TIPO_CUENTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cuenta")
    private Long idTipoCuenta;

    @Column(name = "descripcion")
    private String descripcion;
}
