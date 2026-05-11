package com.sanosysalvos.mascotas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tamanio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tamanio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tamanio")
    private Long idTamanio;

    @Column(name = "descripcion_tamanio")
    private String descripcionTamanio;
}
