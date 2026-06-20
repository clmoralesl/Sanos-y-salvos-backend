package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByTipoReporte_DescripcionIgnoreCaseAndMascota_Raza_Especie_NombreEspecieIgnoreCaseAndIdUbicacionReporteIn(
            String tipo, String especie, List<Long> ubicacionesIds);
}

