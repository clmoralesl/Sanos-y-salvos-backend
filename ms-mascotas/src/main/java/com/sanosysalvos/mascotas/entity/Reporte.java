package com.sanosysalvos.mascotas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_incidente", nullable = false)
    private LocalDateTime fechaIncidente;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }

    @Column(name = "id_ubicacion_reporte")
    private Long idUbicacionReporte;

    @ManyToOne
    @JoinColumn(name = "id_tipo_reporte")
    private TipoReporte tipoReporte;

    @ManyToOne
    @JoinColumn(name = "id_estado_reporte")
    private EstadoReporte estadoReporte;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    private Mascota mascota;
}

