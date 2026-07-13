package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChipStrategyTest {

    private ChipStrategy strategy;
    private MascotaDTO base;
    private MascotaDTO candidato;

    @BeforeEach
    void setUp() {
        strategy = new ChipStrategy();
        base = new MascotaDTO();
        candidato = new MascotaDTO();
    }

    @Test
    void calcularPuntaje_conMismoChip_shouldReturn100() {
        base.setChip("123456789");
        candidato.setChip("123456789");

        double result = strategy.calcularPuntaje(base, candidato);
        assertEquals(100.0, result);
    }

    @Test
    void calcularPuntaje_conDistintoChip_shouldReturn0() {
        base.setChip("123456789");
        candidato.setChip("987654321");

        double result = strategy.calcularPuntaje(base, candidato);
        assertEquals(0.0, result);
    }

    @Test
    void calcularPuntaje_conChipNulo_shouldReturn0() {
        base.setChip("123456789");
        candidato.setChip(null);

        double result = strategy.calcularPuntaje(base, candidato);
        assertEquals(0.0, result);
    }
}
