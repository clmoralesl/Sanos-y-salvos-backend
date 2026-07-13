package com.sanosysalvos.coincidencias.business;

import com.sanosysalvos.coincidencias.business.factory.SimilitudStrategyFactory;
import com.sanosysalvos.coincidencias.business.strategy.SimilitudStrategy;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MotorSimilitudTest {

    @Mock
    private SimilitudStrategyFactory strategyFactory;

    @InjectMocks
    private MotorSimilitud motorSimilitud;

    private MascotaDTO base;
    private MascotaDTO candidato;

    @BeforeEach
    void setUp() {
        base = new MascotaDTO();
        candidato = new MascotaDTO();
    }

    @Test
    void evaluar_diferenteEspecie_shouldReturnZero() {
        base.setEspecie("Perro");
        candidato.setEspecie("Gato");

        double result = motorSimilitud.evaluar(base, candidato);
        assertEquals(0.0, result);
    }

    @Test
    void evaluar_mismaEspecie_shouldCalculateStrategies() {
        base.setEspecie("Perro");
        candidato.setEspecie("Perro");

        SimilitudStrategy strategy1 = mock(SimilitudStrategy.class);
        when(strategy1.calcularPuntaje(base, candidato)).thenReturn(50.0);

        SimilitudStrategy strategy2 = mock(SimilitudStrategy.class);
        when(strategy2.calcularPuntaje(base, candidato)).thenReturn(35.0);

        when(strategyFactory.getStrategies(anyList())).thenReturn(List.of(strategy1, strategy2));

        double result = motorSimilitud.evaluar(base, candidato);
        assertEquals(85.0, result);
    }

    @Test
    void evaluar_especieOtra_shouldCalculateStrategies() {
        base.setEspecie("Otra");
        candidato.setEspecie("Perro");

        SimilitudStrategy strategy = mock(SimilitudStrategy.class);
        when(strategy.calcularPuntaje(base, candidato)).thenReturn(20.0);

        when(strategyFactory.getStrategies(anyList())).thenReturn(List.of(strategy));

        double result = motorSimilitud.evaluar(base, candidato);
        assertEquals(20.0, result);
    }
}