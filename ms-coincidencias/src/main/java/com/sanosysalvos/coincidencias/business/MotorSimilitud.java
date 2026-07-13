package com.sanosysalvos.coincidencias.business;

import com.sanosysalvos.coincidencias.business.factory.CriterioSimilitud;
import com.sanosysalvos.coincidencias.business.factory.SimilitudStrategyFactory;
import com.sanosysalvos.coincidencias.business.strategy.SimilitudStrategy;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MotorSimilitud {

    private final SimilitudStrategyFactory strategyFactory;

    public MotorSimilitud(SimilitudStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public double evaluar(MascotaDTO base, MascotaDTO candidato) {
        // Validación estricta: Si la especie es diferente, la similitud es 0% (un perro nunca hará match con un gato).
        // Excepción: Si alguno de los dos es "Otra", permitimos que otras estrategias evalúen el match.
        if (base.getEspecie() != null && candidato.getEspecie() != null &&
            !base.getEspecie().equalsIgnoreCase(candidato.getEspecie()) &&
            !"Otra".equalsIgnoreCase(base.getEspecie()) &&
            !"Otra".equalsIgnoreCase(candidato.getEspecie())) {
            return 0.0;
        }

        List<CriterioSimilitud> criterios = Arrays.asList(
                CriterioSimilitud.CHIP,
                CriterioSimilitud.RAZA,
                CriterioSimilitud.TAMANIO,
                CriterioSimilitud.EDAD,
                CriterioSimilitud.COLOR
        );

        List<SimilitudStrategy> estrategias = strategyFactory.getStrategies(criterios);

        double total = estrategias.stream()
                .mapToDouble(estrategia -> estrategia.calcularPuntaje(base, candidato))
                .sum();
        
        return Math.max(0.0, Math.min(100.0, total));
    }
}
