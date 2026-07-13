package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RazaStrategyTest {

    private final RazaStrategy strategy = new RazaStrategy();

    @Test
    public void testScoreMatchesExactly() {
        MascotaDTO base = MascotaDTO.builder().raza("Labrador").build();
        MascotaDTO candidato = MascotaDTO.builder().raza("Labrador").build();
        assertEquals(40.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    public void testScoreMismatches() {
        MascotaDTO base = MascotaDTO.builder().raza("Labrador").build();
        MascotaDTO candidato = MascotaDTO.builder().raza("Poodle").build();
        assertEquals(-10.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    public void testScoreWithGenericBreed() {
        MascotaDTO base = MascotaDTO.builder().raza("Mestizo").build();
        MascotaDTO candidato = MascotaDTO.builder().raza("Poodle").build();
        assertEquals(25.0, strategy.calcularPuntaje(base, candidato));
    }

    @Test
    public void testScoreWithNullBreed() {
        MascotaDTO base = MascotaDTO.builder().raza(null).build();
        MascotaDTO candidato = MascotaDTO.builder().raza("Poodle").build();
        assertEquals(0.0, strategy.calcularPuntaje(base, candidato));
    }
}
