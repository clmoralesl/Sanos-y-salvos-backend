package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class RazaStrategy implements SimilitudStrategy {
    
    // Aporta un máximo de 30% a la similitud si es de la misma raza
    private static final double MAX_PUNTAJE = 30.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        if (base.getRaza() != null && base.getRaza().equalsIgnoreCase(candidato.getRaza())) {
            return MAX_PUNTAJE;
        }
        return 0.0;
    }
}
