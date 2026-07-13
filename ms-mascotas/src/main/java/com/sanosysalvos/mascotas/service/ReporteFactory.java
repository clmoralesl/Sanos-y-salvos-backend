package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.entity.Mascota;
import com.sanosysalvos.mascotas.entity.Reporte;
import com.sanosysalvos.mascotas.entity.Usuario;

public interface ReporteFactory {

    /**
     * Factory Method principal para instanciar un Reporte en base al Request DTO.
     */
    Reporte crearReporte(ReporteRequestDTO request, Usuario usuario, Mascota mascota);

    /**
     * Factory Method para el mapeo estandarizado de Entidad a Response DTO.
     */
    ReporteResponseDTO mapearAResponse(Reporte reporte);
}
