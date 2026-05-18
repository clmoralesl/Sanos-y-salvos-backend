package com.sanosysalvos.coincidencias.business;

import com.sanosysalvos.coincidencias.business.strategy.SimilitudStrategy;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotorSimilitud {

    
    private final List<SimilitudStrategy> estrategias;


    public double evaluar(MascotaDTO base, MascotaDTO candidato) {
        return estrategias.stream()
                .mapToDouble(estrategia -> estrategia.calcularPuntaje(base, candidato))
                .sum();
    }
}

