package com.sanosysalvos.msgeo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UBICACION_REPORTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion_reporte")
    private Long id;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMUNA_id_comuna", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Comuna comuna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZONA_GEO_id_zona_geo")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ZonaGeo zonaGeo;
}

