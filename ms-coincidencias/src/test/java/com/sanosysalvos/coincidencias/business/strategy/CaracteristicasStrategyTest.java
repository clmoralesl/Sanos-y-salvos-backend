package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaracteristicasStrategyTest {

    private final CaracteristicasStrategy strategy =
            new CaracteristicasStrategy();

    @Test
    void debeSumarPuntajesSegunImportanciaDeLasCaracteristicas() {

        MascotaDTO base = MascotaDTO.builder()
                .caracteristicas(List.of(
                        "Cicatriz visible",
                        "Patas blancas",
                        "Muy activo"
                ))
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .caracteristicas(List.of(
                        "cicatriz visible",
                        "patas blancas",
                        "muy activo"
                ))
                .build();

        double resultado =
                strategy.calcularPuntaje(base, candidato);

        // Cicatriz: 3.0
        // Patas blancas: 1.5
        // Muy activo: 0.2
        // Total: 4.7
        assertEquals(4.7, resultado, 0.001);
    }

    @Test
    void debeDarPuntajeBajoCuandoSoloCoincideComportamiento() {

        MascotaDTO base = MascotaDTO.builder()
                .caracteristicas(List.of("Muy activo"))
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .caracteristicas(List.of("muy activo"))
                .build();

        double resultado =
                strategy.calcularPuntaje(base, candidato);

        assertEquals(0.2, resultado, 0.001);
    }

    @Test
    void debeRetornarCeroCuandoNoCoincidenLasCaracteristicas() {

        MascotaDTO base = MascotaDTO.builder()
                .caracteristicas(List.of("Cicatriz visible"))
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .caracteristicas(List.of("Muy activo"))
                .build();

        double resultado =
                strategy.calcularPuntaje(base, candidato);

        assertEquals(0.0, resultado, 0.001);
    }

    @Test
    void debeRetornarCeroCuandoNoHayCaracteristicas() {

        MascotaDTO base = MascotaDTO.builder()
                .caracteristicas(List.of())
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .caracteristicas(List.of("Usa collar"))
                .build();

        double resultado =
                strategy.calcularPuntaje(base, candidato);

        assertEquals(0.0, resultado, 0.001);
    }

    @Test
    void noDebeSuperarElMaximoDeDiezPuntos() {

        MascotaDTO base = MascotaDTO.builder()
                .caracteristicas(List.of(
                        "Cicatriz visible",
                        "Mancha en el ojo",
                        "Ojos de distinto color",
                        "Sin cola"
                ))
                .build();

        MascotaDTO candidato = MascotaDTO.builder()
                .caracteristicas(List.of(
                        "Cicatriz visible",
                        "Mancha en el ojo",
                        "Ojos de distinto color",
                        "Sin cola"
                ))
                .build();

        double resultado =
                strategy.calcularPuntaje(base, candidato);

        // La suma sería 12, pero el máximo permitido es 10.
        assertEquals(10.0, resultado, 0.001);
    }
}