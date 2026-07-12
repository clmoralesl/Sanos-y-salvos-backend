package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaracteristicasStrategyTest {

    private final CaracteristicasStrategy strategy = new CaracteristicasStrategy();

    @Test
    void debeRetornar20CuandoTodasLasCaracteristicasCoinciden() {
        MascotaDTO base = MascotaDTO.builder()
                .caracteristicas(List.of("Mancha blanca", "Collar rojo"))
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .caracteristicas(List.of("mancha blanca", "collar rojo"))
                .build();

        assertEquals(20.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornar10CuandoCoincideLaMitad() {
        MascotaDTO base = MascotaDTO.builder()
                .caracteristicas(List.of("Mancha blanca", "Collar rojo"))
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .caracteristicas(List.of("Collar rojo"))
                .build();

        assertEquals(10.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarCeroCuandoNoHayCaracteristicas() {
        MascotaDTO base = MascotaDTO.builder()
                .caracteristicas(List.of())
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .caracteristicas(List.of("Collar rojo"))
                .build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }
}