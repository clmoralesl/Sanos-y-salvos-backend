package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RazaStrategyTest {

    private final RazaStrategy strategy = new RazaStrategy();

    @Test
    void debeRetornarPuntajeMaximoCuandoRazaEsIgual() {
        MascotaDTO base = MascotaDTO.builder().raza("Labrador").build();
        MascotaDTO candidato = MascotaDTO.builder().raza("labrador").build();

        assertEquals(40.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarPuntajeParcialCuandoRazaEsGenerica() {
        MascotaDTO base = MascotaDTO.builder().raza("Mestizo").build();
        MascotaDTO candidato = MascotaDTO.builder().raza("Poodle").build();

        assertEquals(25.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarCeroCuandoRazaEsDistinta() {
        MascotaDTO base = MascotaDTO.builder().raza("Labrador").build();
        MascotaDTO candidato = MascotaDTO.builder().raza("Poodle").build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }
}