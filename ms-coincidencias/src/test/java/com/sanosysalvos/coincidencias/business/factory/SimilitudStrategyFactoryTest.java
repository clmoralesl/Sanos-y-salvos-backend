package com.sanosysalvos.coincidencias.business.factory;

import com.sanosysalvos.coincidencias.business.strategy.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SimilitudStrategyFactoryTest {

    @Mock
    private ChipStrategy chipStrategy;

    @Mock
    private RazaStrategy razaStrategy;

    @Mock
    private TamanioStrategy tamanioStrategy;

    @Mock
    private EdadStrategy edadStrategy;

    @Mock
    private ColorStrategy colorStrategy;

    @InjectMocks
    private SimilitudStrategyFactory factory;

    @Test
    void testGetStrategies_AllStrategies() {
        List<CriterioSimilitud> criterios = Arrays.asList(
                CriterioSimilitud.CHIP,
                CriterioSimilitud.RAZA,
                CriterioSimilitud.TAMANIO,
                CriterioSimilitud.EDAD,
                CriterioSimilitud.COLOR
        );

        List<SimilitudStrategy> strategies = factory.getStrategies(criterios);

        assertEquals(5, strategies.size());
        assertTrue(strategies.contains(chipStrategy));
        assertTrue(strategies.contains(razaStrategy));
        assertTrue(strategies.contains(tamanioStrategy));
        assertTrue(strategies.contains(edadStrategy));
        assertTrue(strategies.contains(colorStrategy));
    }

    @Test
    void testGetStrategies_EmptyList() {
        List<SimilitudStrategy> strategies = factory.getStrategies(Collections.emptyList());
        assertTrue(strategies.isEmpty());
    }
}