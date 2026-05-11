package com.sanosysalvos.coincidencias.business;

import com.sanosysalvos.coincidencias.business.strategy.SimilitudStrategy;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotorSimilitud {

    // Spring Inyecta automáticamente todas las implementaciones de SimilitudStrategy
    private final List<SimilitudStrategy> estrategias;

    /**
     * Evalúa todas las estrategias registradas y suma sus puntajes
     * para obtener el porcentaje de similitud total (0 a 100).
     */
    public double evaluar(MascotaDTO base, MascotaDTO candidato) {
        return estrategias.stream()
                .mapToDouble(estrategia -> estrategia.calcularPuntaje(base, candidato))
                .sum();
    }
}
