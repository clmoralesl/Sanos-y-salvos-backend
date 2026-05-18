package com.sanosysalvos.mascotas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "mascota")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Long idMascota;

    @Column(name = "nombre_mascota")
    private String nombreMascota;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_raza")
    private Raza raza;

    @ManyToOne
    @JoinColumn(name = "id_tamanio")
    private Tamanio tamanio;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fotografia> fotografias;

    @ManyToMany
    @JoinTable(
            name = "mascota_caracteristica",
            joinColumns = @JoinColumn(name = "id_mascota"),
            inverseJoinColumns = @JoinColumn(name = "id_caracteristica")
    )
    private List<Caracteristica> caracteristicas;
}

