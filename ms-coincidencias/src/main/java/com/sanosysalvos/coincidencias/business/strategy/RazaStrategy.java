package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class RazaStrategy implements SimilitudStrategy {

    private static final double MAX_PUNTAJE = 40.0;
    private static final double PARTIAL_PUNTAJE = 20.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        if (base.getRaza() != null && base.getRaza().equalsIgnoreCase(candidato.getRaza())) {
            return MAX_PUNTAJE;
        }
        if (isGeneric(base.getRaza()) || isGeneric(candidato.getRaza())) {
            return PARTIAL_PUNTAJE;
        }
        return 0.0;
    }

    private boolean isGeneric(String breed) {
        if (breed == null) {
            return true;
        }
        String normalized = breed.toLowerCase();
        return normalized.contains("no lo se") || 
               normalized.contains("mestizo") || 
               normalized.contains("otro") || 
               normalized.contains("callejero") || 
               normalized.contains("comun");
    }
}

