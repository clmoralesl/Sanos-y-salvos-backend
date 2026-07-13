package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TamanioStrategyTest {

    private final TamanioStrategy strategy = new TamanioStrategy();

    @Test
    public void testScoreMatchesExactly() {
        MascotaDTO base = MascotaDTO.builder().tamanio("Grande").build();
        MascotaDTO candidato = MascotaDTO.builder().tamanio("Grande").build();
        assertEquals(30.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    public void testScoreCloseMatch() {
        MascotaDTO base = MascotaDTO.builder().tamanio("Grande").build();
        MascotaDTO candidato = MascotaDTO.builder().tamanio("Mediano").build();
        assertEquals(15.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    public void testScoreFarMismatch() {
        MascotaDTO base = MascotaDTO.builder().tamanio("Grande").build();
        MascotaDTO candidato = MascotaDTO.builder().tamanio("Pequeño").build();
        assertEquals(-5.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    public void testScoreWithNullTamanio() {
        MascotaDTO base = MascotaDTO.builder().tamanio(null).build();
        MascotaDTO candidato = MascotaDTO.builder().tamanio("Grande").build();
        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }
}
