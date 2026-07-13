package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class ChipStrategy implements SimilitudStrategy {

    private static final double MAX_PUNTAJE = 100.0;
    
    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        if (base.getChip() != null && !base.getChip().trim().isEmpty() &&
            candidato.getChip() != null && !candidato.getChip().trim().isEmpty()) {
            
            if (base.getChip().trim().equalsIgnoreCase(candidato.getChip().trim())) {
                return MAX_PUNTAJE;
            }
        }
        return 0.0;
    }
}
