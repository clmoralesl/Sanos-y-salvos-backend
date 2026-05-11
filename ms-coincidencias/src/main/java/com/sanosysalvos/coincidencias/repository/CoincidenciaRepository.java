package com.sanosysalvos.coincidencias.repository;

import com.sanosysalvos.coincidencias.domain.entity.Coincidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoincidenciaRepository extends JpaRepository<Coincidencia, Long> {

    // Buscar todas las coincidencias asociadas a un reporte de mascota perdida
    List<Coincidencia> findByReportePerdidaId(Long reportePerdidaId);

    // Buscar todas las coincidencias asociadas a un reporte de hallazgo
    List<Coincidencia> findByReporteHallazgoId(Long reporteHallazgoId);

    // Verificar si ya existe una coincidencia calculada entre dos reportes para evitar duplicidad
    boolean existsByReportePerdidaIdAndReporteHallazgoId(Long reportePerdidaId, Long reporteHallazgoId);
}
