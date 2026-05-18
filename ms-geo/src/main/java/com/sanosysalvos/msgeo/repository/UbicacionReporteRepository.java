package com.sanosysalvos.msgeo.repository;

import com.sanosysalvos.msgeo.model.UbicacionReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionReporteRepository extends JpaRepository<UbicacionReporte, Long> {
}

