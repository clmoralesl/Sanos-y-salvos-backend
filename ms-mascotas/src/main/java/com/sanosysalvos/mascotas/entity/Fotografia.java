package com.sanosysalvos.mascotas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fotografia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fotografia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fotografia")
    private Long idFotografia;

    @Column(name = "url_fotografia")
    private String urlFotografia;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    private Mascota mascota;
}

