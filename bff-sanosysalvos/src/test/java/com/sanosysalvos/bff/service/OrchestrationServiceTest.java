package com.sanosysalvos.bff.service;

import com.sanosysalvos.bff.dto.ReporteDetalleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrchestrationServiceTest {

    @Mock
    private BffIntegrationService integrationService;

    @InjectMocks
    private OrchestrationService orchestrationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void obtenerDetalleCompleto_shouldReturnDetalleCompleto() {
        Long idReporte = 1L;
        String auth0Id = "auth0|123";

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("idMascota", 10L);
        reporte.put("idUbicacionReporte", 20L);
        reporte.put("idUsuario", 30L);
        reporte.put("tipoReporte", "Mascota Perdida");
        reporte.put("estadoReporte", "Activo");
        reporte.put("fechaRegistro", "2023-10-01");
        reporte.put("fechaIncidente", "2023-10-01");

        Map<String, Object> mascota = new HashMap<>();
        mascota.put("nombreMascota", "Firulais");
        mascota.put("descripcion", "Perro amigable");
        mascota.put("nombreRaza", "Mestizo");

        Map<String, Object> ubicacion = new HashMap<>();
        ubicacion.put("latitud", -33.4);
        ubicacion.put("longitud", -70.6);
        Map<String, Object> comuna = new HashMap<>();
        comuna.put("nombre", "Santiago");
        Map<String, Object> region = new HashMap<>();
        region.put("nombre", "Metropolitana");
        comuna.put("region", region);
        ubicacion.put("comuna", comuna);

        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nombre", "Juan Perez");
        usuario.put("email", "juan@test.com");

        when(integrationService.obtenerReporte(idReporte, auth0Id)).thenReturn(reporte);
        when(integrationService.obtenerMascota(10L)).thenReturn(mascota);
        when(integrationService.obtenerUbicacion(20L)).thenReturn(ubicacion);
        when(integrationService.obtenerUsuario(30L)).thenReturn(usuario);

        List<Map<String, Object>> coincidenciasRaw = new ArrayList<>();
        Map<String, Object> coincidencia = new HashMap<>();
        coincidencia.put("reportePerdidaId", 1L);
        coincidencia.put("reporteHallazgoId", 2L);
        coincidencia.put("porcentajeSimilitud", 90.0);
        coincidencia.put("estado", "Pendiente");
        coincidenciasRaw.add(coincidencia);

        when(integrationService.obtenerCoincidenciasPorPerdida(idReporte)).thenReturn(coincidenciasRaw);

        Map<String, Object> matchedReport = new HashMap<>();
        matchedReport.put("idMascota", 11L);
        matchedReport.put("idUbicacionReporte", 21L);
        matchedReport.put("estadoReporte", "Activo");
        when(integrationService.obtenerReporte(2L, auth0Id)).thenReturn(matchedReport);

        Map<String, Object> matchMascota = new HashMap<>();
        matchMascota.put("nombreMascota", "Fido");
        when(integrationService.obtenerMascota(11L)).thenReturn(matchMascota);

        Map<String, Object> matchUbicacion = new HashMap<>();
        when(integrationService.obtenerUbicacion(21L)).thenReturn(matchUbicacion);

        ReporteDetalleDTO result = orchestrationService.obtenerDetalleCompleto(idReporte, auth0Id);

        assertNotNull(result);
        assertEquals(1L, result.getIdReporte());
        assertEquals("Juan Perez", result.getUsuario().getNombre());
        assertEquals("Firulais", result.getMascota().getNombre());
        assertEquals("Santiago", result.getUbicacion().getComuna());
        assertEquals(1, result.getCoincidencias().size());
        assertEquals("Fido", result.getCoincidencias().get(0).getNombreMascota());
    }
}
