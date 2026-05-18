package com.sanosysalvos.coincidencias.domain.entity;

import com.sanosysalvos.coincidencias.domain.enums.EstadoCoincidencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COINCIDENCIA")
public class Coincidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reporte_perdida_id", nullable = false)
    private Long reportePerdidaId;

    @Column(name = "reporte_hallazgo_id", nullable = false)
    private Long reporteHallazgoId;

    @Column(name = "porcentaje_similitud", nullable = false)
    private Double porcentajeSimilitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_coincidencia", nullable = false)
    private EstadoCoincidencia estado;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}

