package com.sanosysalvos.coincidencias.repository;

import com.sanosysalvos.coincidencias.domain.entity.Coincidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoincidenciaRepository extends JpaRepository<Coincidencia, Long> {

    
    List<Coincidencia> findByReportePerdidaId(Long reportePerdidaId);

    
    List<Coincidencia> findByReporteHallazgoId(Long reporteHallazgoId);

    
    boolean existsByReportePerdidaIdAndReporteHallazgoId(Long reportePerdidaId, Long reporteHallazgoId);
}

