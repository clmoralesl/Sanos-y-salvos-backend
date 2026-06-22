package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class EdadStrategy implements SimilitudStrategy {

    private static final double MAX_PUNTAJE = 15.0;
    private static final double PARTIAL_PUNTAJE = 7.5;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        if (base.getEdadAproximada() == null || candidato.getEdadAproximada() == null) {
            return 0.0;
        }

        if (base.getEdadAproximada().equalsIgnoreCase(candidato.getEdadAproximada())) {
            return MAX_PUNTAJE;
        }

        int valBase = getScaleValue(base.getEdadAproximada());
        int valCandidato = getScaleValue(candidato.getEdadAproximada());

        if (valBase > 0 && valCandidato > 0 && Math.abs(valBase - valCandidato) == 1) {
            return PARTIAL_PUNTAJE;
        }

        return 0.0;
    }

    private int getScaleValue(String label) {
        String normalized = label.toLowerCase();
        if (normalized.contains("0-1")) {
            return 1;
        }
        if (normalized.contains("1-3")) {
            return 2;
        }
        if (normalized.contains("3-7")) {
            return 3;
        }
        if (normalized.contains("7+")) {
            return 4;
        }
        return 0;
    }
}
