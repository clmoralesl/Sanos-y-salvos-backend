package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorStrategyTest {

    private final ColorStrategy strategy = new ColorStrategy();

    @Test
    void debeRetornar20CuandoColorPrimarioYSecundarioCoinciden() {
        MascotaDTO base = MascotaDTO.builder()
                .colorPrimario("Negro")
                .colorSecundario("Blanco")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .colorPrimario("negro")
                .colorSecundario("blanco")
                .build();

        assertEquals(20.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornar10CuandoColorPrimarioEsSimilar() {
        MascotaDTO base = MascotaDTO.builder()
                .colorPrimario("Negro")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .colorPrimario("Gris")
                .build();

        assertEquals(10.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornar12CuandoColoresEstanInvertidos() {
        MascotaDTO base = MascotaDTO.builder()
                .colorPrimario("Negro")
                .colorSecundario("Blanco")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .colorPrimario("Blanco")
                .colorSecundario("Negro")
                .build();

        assertEquals(12.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarCeroCuandoNoHayColoresPrimarios() {
        MascotaDTO base = MascotaDTO.builder().build();
        MascotaDTO candidato = MascotaDTO.builder().build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }
}