package com.sanosysalvos.mascotas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ESPECIE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especie")
    private Long idEspecie;

    @Column(name = "nombre_especie")
    private String nombreEspecie;
}

