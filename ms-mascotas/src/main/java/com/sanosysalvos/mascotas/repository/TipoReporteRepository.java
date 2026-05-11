package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.TipoReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoReporteRepository extends JpaRepository<TipoReporte, Long> {}
