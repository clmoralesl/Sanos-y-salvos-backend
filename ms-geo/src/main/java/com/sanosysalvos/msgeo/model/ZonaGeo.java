package com.sanosysalvos.msgeo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ZONA_GEO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZonaGeo {

    @Id
    @Column(name = "id_zona_geo", nullable = false, length = 50)
    private String id;

}
