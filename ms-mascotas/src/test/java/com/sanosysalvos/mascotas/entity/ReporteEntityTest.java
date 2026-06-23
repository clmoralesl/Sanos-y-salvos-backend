package com.sanosysalvos.mascotas.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReporteEntityTest {

    @Test
    public void testOnCreateCallback() {
        Reporte reporte = new Reporte();
        assertNull(reporte.getFechaRegistro());

        reporte.onCreate();

        assertNotNull(reporte.getFechaRegistro());
    }
}
