package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {}
