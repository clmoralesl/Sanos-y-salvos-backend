package com.sanosysalvos.coincidencias.service;

public interface CoincidenciaService {
    /**
     * Inicia el flujo de evaluación de coincidencias para un reporte nuevo.
     * @param reporteId El ID del reporte (pérdida o hallazgo) que sirve de trigger.
     */
    void procesarReporte(Long reporteId);
}
