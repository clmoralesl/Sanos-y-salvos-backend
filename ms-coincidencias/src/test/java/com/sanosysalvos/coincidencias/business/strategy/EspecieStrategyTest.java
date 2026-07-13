package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EspecieStrategyTest {

    private final EspecieStrategy strategy = new EspecieStrategy();

    @Test
    void debeRetornarCeroCuandoEspecieEsIgual() {
        MascotaDTO base = MascotaDTO.builder().especie("Perro").build();
        MascotaDTO candidato = MascotaDTO.builder().especie("Perro").build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarCeroCuandoUnaEspecieEsOtra() {
        MascotaDTO base = MascotaDTO.builder().especie("Otra").build();
        MascotaDTO candidato = MascotaDTO.builder().especie("Perro").build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarInfinityNegativoCuandoEspecieEsDistinta() {
        MascotaDTO base = MascotaDTO.builder().especie("Perro").build();
        MascotaDTO candidato = MascotaDTO.builder().especie("Gato").build();

        assertEquals(Double.NEGATIVE_INFINITY,
                strategy.calcularPuntaje(base, candidato));
    }
}