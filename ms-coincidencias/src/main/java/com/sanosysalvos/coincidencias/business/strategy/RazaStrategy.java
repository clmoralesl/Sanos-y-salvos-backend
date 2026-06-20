package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class RazaStrategy implements SimilitudStrategy {

    
    private static final double MAX_PUNTAJE = 50.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        if (base.getRaza() != null && base.getRaza().equalsIgnoreCase(candidato.getRaza())) {
            return MAX_PUNTAJE;
        }
        return 0.0;
    }
}

