package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class TamanioStrategy implements SimilitudStrategy {

    private static final double MAX_PUNTAJE = 30.0;
    private static final double PARTIAL_PUNTAJE = 15.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        if (base.getTamanio() == null || candidato.getTamanio() == null) {
            return 0.0;
        }

        if (base.getTamanio().equalsIgnoreCase(candidato.getTamanio())) {
            return MAX_PUNTAJE;
        }

        int valBase = getScaleValue(base.getTamanio());
        int valCandidato = getScaleValue(candidato.getTamanio());

        if (valBase > 0 && valCandidato > 0 && Math.abs(valBase - valCandidato) == 1) {
            return PARTIAL_PUNTAJE;
        }

        return 0.0;
    }

    private int getScaleValue(String label) {
        String normalized = label.toLowerCase();
        if (normalized.contains("peque") || normalized.contains("pequeño")) {
            return 1;
        }
        if (normalized.contains("median")) {
            return 2;
        }
        if (normalized.contains("grand")) {
            return 3;
        }
        if (normalized.contains("gigant")) {
            return 4;
        }
        return 0;
    }
}

