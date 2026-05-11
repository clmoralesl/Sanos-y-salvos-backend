package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import java.util.List;

public interface ReporteService {
    ReporteResponseDTO crearReporte(ReporteRequestDTO request, String auth0Id);
    List<ReporteResponseDTO> obtenerTodosLosReportes();
    ReporteResponseDTO obtenerReportePorId(Long id);
    ReporteResponseDTO cerrarReporte(Long idReporte, String auth0Id);
    ReporteResponseDTO actualizarReporte(Long idReporte, ReporteRequestDTO request, String auth0Id);
    void eliminarReporte(Long idReporte, String auth0Id);
}
