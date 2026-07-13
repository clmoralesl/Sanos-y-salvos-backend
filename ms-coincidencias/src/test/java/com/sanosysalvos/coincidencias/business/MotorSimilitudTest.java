package com.sanosysalvos.coincidencias.business;

import com.sanosysalvos.coincidencias.business.factory.CriterioSimilitud;
import com.sanosysalvos.coincidencias.business.factory.SimilitudStrategyFactory;
import com.sanosysalvos.coincidencias.business.strategy.SimilitudStrategy;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MotorSimilitudTest {

    private final MascotaDTO base = MascotaDTO.builder().build();
    private final MascotaDTO candidato = MascotaDTO.builder().build();

    @Test
    void debeSumarPuntajesDeTodasLasEstrategias() {

        SimilitudStrategyFactory factory = mock(SimilitudStrategyFactory.class);
        SimilitudStrategy estrategia = mock(SimilitudStrategy.class);

        when(factory.crear(any(CriterioSimilitud.class)))
                .thenReturn(estrategia);

        when(estrategia.calcularPuntaje(base, candidato))
                .thenReturn(10.0);

        MotorSimilitud motor = new MotorSimilitud(factory);

        assertEquals(70.0, motor.evaluar(base, candidato));

        verify(factory, times(7)).crear(any(CriterioSimilitud.class));
    }

    @Test
    void debeLimitarResultadoAMaximo100() {

        SimilitudStrategyFactory factory = mock(SimilitudStrategyFactory.class);
        SimilitudStrategy estrategia = mock(SimilitudStrategy.class);

        when(factory.crear(any(CriterioSimilitud.class)))
                .thenReturn(estrategia);

        when(estrategia.calcularPuntaje(base, candidato))
                .thenReturn(20.0);

        MotorSimilitud motor = new MotorSimilitud(factory);

        assertEquals(100.0, motor.evaluar(base, candidato));
    }

    @Test
    void debeRetornarCeroCuandoEspecieDescartaCandidato() {

        SimilitudStrategyFactory factory = mock(SimilitudStrategyFactory.class);
        SimilitudStrategy estrategiaEspecie = mock(SimilitudStrategy.class);

        when(factory.crear(CriterioSimilitud.ESPECIE))
                .thenReturn(estrategiaEspecie);

        when(estrategiaEspecie.calcularPuntaje(base, candidato))
                .thenReturn(Double.NEGATIVE_INFINITY);

        MotorSimilitud motor = new MotorSimilitud(factory);

        assertEquals(0.0, motor.evaluar(base, candidato));

        verify(factory, times(1)).crear(CriterioSimilitud.ESPECIE);
        verifyNoMoreInteractions(factory);
    }

    @Test
    void debeEvitarResultadoNegativo() {

        SimilitudStrategyFactory factory = mock(SimilitudStrategyFactory.class);
        SimilitudStrategy estrategia = mock(SimilitudStrategy.class);

        when(factory.crear(any(CriterioSimilitud.class)))
                .thenReturn(estrategia);

        when(estrategia.calcularPuntaje(base, candidato))
                .thenReturn(-10.0);

        MotorSimilitud motor = new MotorSimilitud(factory);

        assertEquals(0.0, motor.evaluar(base, candidato));
    }
}