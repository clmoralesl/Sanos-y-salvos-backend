package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EdadStrategyTest {

    private final EdadStrategy strategy = new EdadStrategy();

    @Test
    void debeRetornar5CuandoEdadEsIgual() {
        MascotaDTO base = MascotaDTO.builder()
                .edadAproximada("1-3")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .edadAproximada("1-3")
                .build();

        assertEquals(5.0, strategy.calcularPuntaje(base, candidato), 0.001);
    }

    @Test
    void debeRetornar2Punto5CuandoEdadesSonContiguas() {
        MascotaDTO base = MascotaDTO.builder()
                .edadAproximada("1-3")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .edadAproximada("3-7")
                .build();

        assertEquals(2.5, strategy.calcularPuntaje(base, candidato), 0.001);
    }

    @Test
    void debeRetornarMenos1CuandoEdadesSonDistintas() {
        MascotaDTO base = MascotaDTO.builder()
                .edadAproximada("0-1")
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .edadAproximada("7+")
                .build();

        assertEquals(-1.0, strategy.calcularPuntaje(base, candidato), 0.001);
    }

    @Test
    void debeRetornarCeroCuandoEdadEsNull() {
        MascotaDTO base = MascotaDTO.builder().build();

        MascotaDTO candidato = MascotaDTO.builder()
                .edadAproximada("1-3")
                .build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato), 0.001);
    }
}