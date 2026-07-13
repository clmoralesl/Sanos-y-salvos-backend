package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class TamanioStrategy implements SimilitudStrategy {

    private static final double PUNTAJE_IGUAL = 15.0;
    private static final double PUNTAJE_CERCANO = 7.0;
    private static final double PENALIZACION_DISTINTO = -2.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {

        if (base.getTamanio() == null || candidato.getTamanio() == null) {
            return 0.0;
        }

        if (base.getTamanio().equalsIgnoreCase(candidato.getTamanio())) {
            return PUNTAJE_IGUAL;
        }

        int valBase = getScaleValue(base.getTamanio());
        int valCandidato = getScaleValue(candidato.getTamanio());

        if (valBase == 0 || valCandidato == 0) {
            return 0.0;
        }

        if (Math.abs(valBase - valCandidato) == 1) {
            return PUNTAJE_CERCANO;
        }

        return PENALIZACION_DISTINTO;
    }

    private int getScaleValue(String label) {

        String normalized = label
                .trim()
                .toLowerCase();

        if (normalized.contains("peque")) {
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