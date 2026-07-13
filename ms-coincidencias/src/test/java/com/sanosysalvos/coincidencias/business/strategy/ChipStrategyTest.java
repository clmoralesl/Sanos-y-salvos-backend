package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChipStrategyTest {

    private final ChipStrategy strategy = new ChipStrategy();

    @Test
    void debeRetornarPuntajeCuandoChipEsIgual() {
        MascotaDTO base = MascotaDTO.builder()
                .numeroChip("123-456-789")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .numeroChip("123456789")
                .build();

        assertEquals(30.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarPenalizacionCuandoChipEsDistinto() {
        MascotaDTO base = MascotaDTO.builder()
                .numeroChip("123456789")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .numeroChip("987654321")
                .build();

        assertEquals(-15.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarCeroCuandoFaltaUnChip() {
        MascotaDTO base = MascotaDTO.builder()
                .numeroChip(null)
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .numeroChip("123456789")
                .build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }
}