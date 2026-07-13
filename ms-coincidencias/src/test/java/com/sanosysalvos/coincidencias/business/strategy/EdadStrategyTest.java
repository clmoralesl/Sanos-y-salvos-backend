package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EdadStrategyTest {

    private final EdadStrategy strategy = new EdadStrategy();

    @Test
    void debeRetornar10CuandoEdadEsIgual() {
        MascotaDTO base = MascotaDTO.builder()
                .edadAproximada("1-3")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .edadAproximada("1-3")
                .build();

        assertEquals(10.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornar5CuandoEdadesSonContiguas() {
        MascotaDTO base = MascotaDTO.builder()
                .edadAproximada("1-3")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .edadAproximada("3-7")
                .build();

        assertEquals(5.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarPenalizacionCuandoEdadesSonDistintas() {
        MascotaDTO base = MascotaDTO.builder()
                .edadAproximada("0-1")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .edadAproximada("7+")
                .build();

        assertEquals(-2.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarCeroCuandoEdadEsNull() {
        MascotaDTO base = MascotaDTO.builder().build();

        MascotaDTO candidato = MascotaDTO.builder()
                .edadAproximada("1-3")
                .build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }
}