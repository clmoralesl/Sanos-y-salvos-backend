package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.EstadoReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoReporteRepository extends JpaRepository<EstadoReporte, Long> {
    EstadoReporte findByDescripcion(String descripcion);
}

