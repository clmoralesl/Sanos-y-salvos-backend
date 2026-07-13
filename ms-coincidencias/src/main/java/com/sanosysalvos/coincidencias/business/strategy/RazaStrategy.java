package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class RazaStrategy implements SimilitudStrategy {

    private static final double PUNTAJE_IGUAL = 25.0;
    private static final double PENALIZACION_DISTINTA = -10.0;
    private static final double PUNTAJE_NEUTRO = 0.0;
    private static final String OTRA = "Otra";

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {

        String razaBase = base.getRaza();
        String razaCandidato = candidato.getRaza();

        if (razaBase == null || razaCandidato == null) {
            return PUNTAJE_NEUTRO;
        }

        if (OTRA.equalsIgnoreCase(razaBase)
                || OTRA.equalsIgnoreCase(razaCandidato)) {
            return PUNTAJE_NEUTRO;
        }

        if (razaBase.equalsIgnoreCase(razaCandidato)) {
            return PUNTAJE_IGUAL;
        }

        return PENALIZACION_DISTINTA;
    }
}