package com.sanosysalvos.coincidencias.business.strategy;


import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class ChipStrategy implements SimilitudStrategy {

    private static final double PUNTAJE_IGUAL = 30.0;
    private static final double PENALIZACION_DISTINTA = -15.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {

        String chipBase = normalizar(base.getNumeroChip());
        String chipCandidato = normalizar(candidato.getNumeroChip());

        if (chipBase == null || chipCandidato == null) {
            return 0.0;
        }

        if (chipBase.equals(chipCandidato)) {
            return PUNTAJE_IGUAL;
        }

        return PENALIZACION_DISTINTA;
    }

    private String normalizar(String chip) {
        if (chip == null) {
            return null;
        }

        String resultado = chip
                .trim()
                .replaceAll("[\\s-]", "")
                .toUpperCase();

        return resultado.isBlank() ? null : resultado;
    }
}