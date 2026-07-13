package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class RazaStrategy implements SimilitudStrategy {

    private static final double MAX_PUNTAJE = 40.0;
    private static final double PARTIAL_PUNTAJE = 25.0;
    private static final double PENALIZACION_DISTINTA = -10.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        String razaBase = base.getRaza();
        String razaCandidato = candidato.getRaza();

        if (razaBase == null || razaCandidato == null) {
            return 0.0;
        }

        if (razaBase.equalsIgnoreCase(razaCandidato)) {
            return MAX_PUNTAJE;
        }

        if (isGeneric(razaBase) || isGeneric(razaCandidato)) {
            return PARTIAL_PUNTAJE;
        }

        return PENALIZACION_DISTINTA;
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

