package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class EspecieStrategy implements SimilitudStrategy {

    private static final String OTRA = "Otra";

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {

        if (base.getEspecie() == null || candidato.getEspecie() == null) {
            return 0.0;
        }

        if (OTRA.equalsIgnoreCase(base.getEspecie())
                || OTRA.equalsIgnoreCase(candidato.getEspecie())) {
            return 0.0;
        }

        if (!base.getEspecie().equalsIgnoreCase(candidato.getEspecie())) {
            return Double.NEGATIVE_INFINITY;
        }

        return 0.0;
    }
}