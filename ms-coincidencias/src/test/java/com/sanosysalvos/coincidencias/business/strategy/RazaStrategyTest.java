package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RazaStrategyTest {

    private final RazaStrategy strategy = new RazaStrategy();

    @Test
    void debeRetornarPuntajeCuandoRazaEsIgual() {
        MascotaDTO base = MascotaDTO.builder().raza("Labrador").build();
        MascotaDTO candidato = MascotaDTO.builder().raza("Labrador").build();

        assertEquals(25.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarPuntajeNeutroCuandoRazaEsOtra() {
        MascotaDTO base = MascotaDTO.builder().raza("Otra").build();
        MascotaDTO candidato = MascotaDTO.builder().raza("Poodle").build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarPenalizacionCuandoRazaEsDistinta() {
        MascotaDTO base = MascotaDTO.builder().raza("Labrador").build();
        MascotaDTO candidato = MascotaDTO.builder().raza("Poodle").build();

        assertEquals(-10.0, strategy.calcularPuntaje(base, candidato));
    }
}