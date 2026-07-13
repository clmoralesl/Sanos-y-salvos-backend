package com.sanosysalvos.coincidencias.business.factory;

import com.sanosysalvos.coincidencias.business.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimilitudStrategyFactory {

    @Autowired
    private ChipStrategy chipStrategy;

    @Autowired
    private RazaStrategy razaStrategy;

    @Autowired
    private TamanioStrategy tamanioStrategy;

    @Autowired
    private EdadStrategy edadStrategy;

    @Autowired
    private ColorStrategy colorStrategy;

    public List<SimilitudStrategy> getStrategies(List<CriterioSimilitud> criterios) {
        List<SimilitudStrategy> strategies = new ArrayList<>();
        for (CriterioSimilitud criterio : criterios) {
            switch (criterio) {
                case CHIP:
                    strategies.add(chipStrategy);
                    break;
                case RAZA:
                    strategies.add(razaStrategy);
                    break;
                case TAMANIO:
                    strategies.add(tamanioStrategy);
                    break;
                case EDAD:
                    strategies.add(edadStrategy);
                    break;
                case COLOR:
                    strategies.add(colorStrategy);
                    break;
            }
        }
        return strategies;
    }
}
