package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorStrategyTest {

    private ColorStrategy strategy;
    private MascotaDTO base;
    private MascotaDTO candidato;

    @BeforeEach
    void setUp() {
        strategy = new ColorStrategy();
        base = new MascotaDTO();
        candidato = new MascotaDTO();
    }

    @Test
    void calcularPuntaje_conMismosColores_shouldReturn15() {
        base.setColorPrimario("Negro");
        base.setColorSecundario("Blanco");
        candidato.setColorPrimario("Negro");
        candidato.setColorSecundario("Blanco");

        double result = strategy.calcularPuntaje(base, candidato);
        assertEquals(15.0, result);
    }

    @Test
    void calcularPuntaje_conColoresSimilares_shouldReturn7() {
        base.setColorPrimario("Cafe");
        base.setColorSecundario("Blanco");
        candidato.setColorPrimario("Amarillo");
        candidato.setColorSecundario("Blanco");

        double result = strategy.calcularPuntaje(base, candidato);
        assertEquals(7.0, result); // 5 (similar primario) + 2 (igual secundario)
    }

    @Test
    void calcularPuntaje_conColoresInvertidos_shouldReturn8() {
        base.setColorPrimario("Negro");
        base.setColorSecundario("Blanco");
        candidato.setColorPrimario("Blanco");
        candidato.setColorSecundario("Negro");

        double result = strategy.calcularPuntaje(base, candidato);
        assertEquals(8.0, result);
    }

    @Test
    void calcularPuntaje_conColoresVacios_shouldReturn0() {
        base.setColorPrimario(null);
        base.setColorSecundario(null);
        candidato.setColorPrimario("");
        candidato.setColorSecundario("ninguno");

        double result = strategy.calcularPuntaje(base, candidato);
        assertEquals(0.0, result);
    }
}