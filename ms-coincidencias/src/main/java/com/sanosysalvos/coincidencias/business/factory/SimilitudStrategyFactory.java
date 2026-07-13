package com.sanosysalvos.coincidencias.business.factory;

import com.sanosysalvos.coincidencias.business.strategy.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimilitudStrategyFactory {

    private final EspecieStrategy especieStrategy;
    private final ChipStrategy chipStrategy;
    private final RazaStrategy razaStrategy;
    private final ColorStrategy colorStrategy;
    private final TamanioStrategy tamanioStrategy;
    private final EdadStrategy edadStrategy;
    private final CaracteristicasStrategy caracteristicasStrategy;

    public SimilitudStrategy crear(CriterioSimilitud criterio) {

        return switch (criterio) {
            case ESPECIE -> especieStrategy;
            case CHIP -> chipStrategy;
            case RAZA -> razaStrategy;
            case COLOR -> colorStrategy;
            case TAMANIO -> tamanioStrategy;
            case EDAD -> edadStrategy;
            case CARACTERISTICAS -> caracteristicasStrategy;
        };
    }
}