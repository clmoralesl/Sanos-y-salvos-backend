package com.sanosysalvos.coincidencias.business;

import com.sanosysalvos.coincidencias.business.factory.CriterioSimilitud;
import com.sanosysalvos.coincidencias.business.factory.SimilitudStrategyFactory;
import com.sanosysalvos.coincidencias.business.strategy.SimilitudStrategy;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotorSimilitud {

    private final SimilitudStrategyFactory factory;

    private static final List<CriterioSimilitud> CRITERIOS = List.of(
            CriterioSimilitud.ESPECIE,
            CriterioSimilitud.CHIP,
            CriterioSimilitud.RAZA,
            CriterioSimilitud.COLOR,
            CriterioSimilitud.TAMANIO,
            CriterioSimilitud.EDAD,
            CriterioSimilitud.CARACTERISTICAS
    );

    public double evaluar(MascotaDTO base, MascotaDTO candidato) {

        double total = 0.0;

        for (CriterioSimilitud criterio : CRITERIOS) {

            SimilitudStrategy estrategia = factory.crear(criterio);

            double puntaje = estrategia.calcularPuntaje(base, candidato);

            if (puntaje == Double.NEGATIVE_INFINITY) {
                return 0.0;
            }

            total += puntaje;
        }

        return Math.max(0.0, Math.min(100.0, total));
    }
}