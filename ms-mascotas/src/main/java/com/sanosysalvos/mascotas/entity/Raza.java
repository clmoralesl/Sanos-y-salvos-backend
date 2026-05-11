package com.sanosysalvos.mascotas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RAZA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Raza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_raza")
    private Long idRaza;

    @Column(name = "nombre_raza")
    private String nombreRaza;

    @ManyToOne
    @JoinColumn(name = "id_especie")
    private Especie especie;
}
