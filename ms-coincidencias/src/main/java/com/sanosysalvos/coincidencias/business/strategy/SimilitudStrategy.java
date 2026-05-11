package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;

public interface SimilitudStrategy {
    /**
     * Calcula un puntaje de similitud (parte de un 100% total)
     * basándose en una cualidad específica de la mascota.
     */
    double calcularPuntaje(MascotaDTO base, MascotaDTO candidato);
}
