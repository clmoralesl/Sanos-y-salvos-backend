package com.sanosysalvos.coincidencias.business;

import com.sanosysalvos.coincidencias.business.factory.SimilitudStrategyFactory;
import com.sanosysalvos.coincidencias.business.strategy.SimilitudStrategy;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MotorSimilitudTest {

    @Test
    void debeSumarPuntajesDeTodasLasEstrategias() {
        SimilitudStrategy s1 = mock(SimilitudStrategy.class);
        SimilitudStrategy s2 = mock(SimilitudStrategy.class);
        SimilitudStrategyFactory factory = mock(SimilitudStrategyFactory.class);

        when(factory.getStrategies(any())).thenReturn(List.of(s1, s2));

        MascotaDTO base = MascotaDTO.builder().build();
        MascotaDTO candidato = MascotaDTO.builder().build();

        when(s1.calcularPuntaje(base, candidato)).thenReturn(30.0);
        when(s2.calcularPuntaje(base, candidato)).thenReturn(20.0);

        MotorSimilitud motor = new MotorSimilitud(factory);

        assertEquals(50.0, motor.evaluar(base, candidato));
    }

    @Test
    void debeLimitarResultadoAMaximo100() {
        SimilitudStrategy s1 = mock(SimilitudStrategy.class);
        SimilitudStrategy s2 = mock(SimilitudStrategy.class);
        SimilitudStrategyFactory factory = mock(SimilitudStrategyFactory.class);

        when(factory.getStrategies(any())).thenReturn(List.of(s1, s2));

        MascotaDTO base = MascotaDTO.builder().build();
        MascotaDTO candidato = MascotaDTO.builder().build();

        when(s1.calcularPuntaje(base, candidato)).thenReturn(70.0);
        when(s2.calcularPuntaje(base, candidato)).thenReturn(50.0);

        MotorSimilitud motor = new MotorSimilitud(factory);

        assertEquals(100.0, motor.evaluar(base, candidato));
    }

    @Test
    void debeRetornarCeroCuandoNoHayPuntaje() {
        SimilitudStrategy s1 = mock(SimilitudStrategy.class);
        SimilitudStrategyFactory factory = mock(SimilitudStrategyFactory.class);

        when(factory.getStrategies(any())).thenReturn(List.of(s1));

        MascotaDTO base = MascotaDTO.builder().build();
        MascotaDTO candidato = MascotaDTO.builder().build();

        when(s1.calcularPuntaje(base, candidato)).thenReturn(0.0);

        MotorSimilitud motor = new MotorSimilitud(factory);

        assertEquals(0.0, motor.evaluar(base, candidato));
    }
}