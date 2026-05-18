package com.sanosysalvos.mascotas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_reporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_reporte")
    private Long idTipoReporte;

    @Column(name = "descripcion")
    private String descripcion;
}

