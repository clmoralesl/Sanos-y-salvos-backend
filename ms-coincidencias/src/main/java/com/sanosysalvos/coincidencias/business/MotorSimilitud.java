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
        // Validación estricta: Si la especie es diferente, la similitud es 0% (un perro nunca hará match con un gato).
        if (base.getEspecie() != null && candidato.getEspecie() != null &&
            !base.getEspecie().equalsIgnoreCase(candidato.getEspecie())) {
            return 0.0;
        }

        double total = estrategias.stream()
                .mapToDouble(estrategia -> estrategia.calcularPuntaje(base, candidato))
                .sum();
        return Math.min(100.0, total);
    }
}
