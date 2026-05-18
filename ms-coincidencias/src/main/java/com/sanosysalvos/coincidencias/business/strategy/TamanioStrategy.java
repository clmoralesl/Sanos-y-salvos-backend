package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class TamanioStrategy implements SimilitudStrategy {

    
    private static final double MAX_PUNTAJE = 20.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        if (base.getTamanio() != null && base.getTamanio().equalsIgnoreCase(candidato.getTamanio())) {
            return MAX_PUNTAJE;
        }
        return 0.0;
    }
}

