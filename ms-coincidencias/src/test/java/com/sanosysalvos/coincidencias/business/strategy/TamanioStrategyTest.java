package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TamanioStrategyTest {

    private final TamanioStrategy strategy = new TamanioStrategy();

    @Test
    void debeRetornarPuntajeMaximoCuandoTamanioEsIgual() {
        MascotaDTO base = MascotaDTO.builder().tamanio("Mediano").build();
        MascotaDTO candidato = MascotaDTO.builder().tamanio("mediano").build();

        assertEquals(30.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarPuntajeParcialCuandoTamaniosSonCercanos() {
        MascotaDTO base = MascotaDTO.builder().tamanio("Pequeño").build();
        MascotaDTO candidato = MascotaDTO.builder().tamanio("Mediano").build();

        assertEquals(15.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    void debeRetornarCeroCuandoTamaniosSonMuyDistintos() {
        MascotaDTO base = MascotaDTO.builder().tamanio("Pequeño").build();
        MascotaDTO candidato = MascotaDTO.builder().tamanio("Grande").build();

        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }
}
