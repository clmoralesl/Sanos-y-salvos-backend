package com.sanosysalvos.bff.service;

import com.sanosysalvos.bff.client.CoincidenciasClient;
import com.sanosysalvos.bff.client.GeoClient;
import com.sanosysalvos.bff.client.MascotasClient;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BffIntegrationServiceTest {

    @Mock
    private MascotasClient mascotasClient;

    @Mock
    private GeoClient geoClient;

    @Mock
    private CoincidenciasClient coincidenciasClient;

    @InjectMocks
    private BffIntegrationService service;

    private Map<String, Object> mockMap;

    @BeforeEach
    void setUp() {
        mockMap = new HashMap<>();
        mockMap.put("id", 1L);
    }

    @Test
    void obtenerReporte_shouldReturnMap() {
        when(mascotasClient.obtenerReporte(1L, "auth0|123")).thenReturn(mockMap);
        Map<String, Object> result = service.obtenerReporte(1L, "auth0|123");
        assertEquals(mockMap, result);
    }

    @Test
    void fallbackObtenerReporte_shouldReturnFallbackMap() {
        Map<String, Object> result = service.fallbackObtenerReporte(1L, "auth0|123", new RuntimeException("Error"));
        assertEquals(1L, result.get("idReporte"));
        assertEquals("Desconocido (Fallo de servicio)", result.get("estadoReporte"));
    }

    @Test
    void obtenerMascota_shouldReturnMap() {
        when(mascotasClient.obtenerMascota(1L)).thenReturn(mockMap);
        Map<String, Object> result = service.obtenerMascota(1L);
        assertEquals(mockMap, result);
    }

    @Test
    void fallbackObtenerMascota_shouldReturnFallbackMap() {
        Map<String, Object> result = service.fallbackObtenerMascota(1L, new RuntimeException("Error"));
        assertEquals(1L, result.get("idMascota"));
        assertEquals("Datos de mascota no disponibles temporalmente", result.get("nombreMascota"));
    }

    @Test
    void obtenerUbicacion_shouldReturnMap() {
        when(geoClient.obtenerUbicacion(1L)).thenReturn(mockMap);
        Map<String, Object> result = service.obtenerUbicacion(1L);
        assertEquals(mockMap, result);
    }

    @Test
    void fallbackObtenerUbicacion_shouldReturnFallbackMap() {
        Map<String, Object> result = service.fallbackObtenerUbicacion(1L, new RuntimeException("Error"));
        assertEquals(1L, result.get("idUbicacion"));
        assertEquals("Ubicación temporalmente no disponible", result.get("direccionEspecifica"));
    }

    @Test
    void obtenerUsuario_shouldReturnMap() {
        when(mascotasClient.obtenerUsuario(1L)).thenReturn(mockMap);
        Map<String, Object> result = service.obtenerUsuario(1L);
        assertEquals(mockMap, result);
    }

    @Test
    void fallbackObtenerUsuario_shouldReturnFallbackMap() {
        Map<String, Object> result = service.fallbackObtenerUsuario(1L, new RuntimeException("Error"));
        assertEquals(1L, result.get("idUsuario"));
        assertEquals("Usuario no disponible", result.get("nombre"));
    }

    @Test
    void obtenerCoincidenciasPorPerdida_shouldReturnList() {
        List<Map<String, Object>> mockList = new ArrayList<>();
        mockList.add(mockMap);
        when(coincidenciasClient.obtenerPorPerdida(1L)).thenReturn(mockList);
        List<Map<String, Object>> result = service.obtenerCoincidenciasPorPerdida(1L);
        assertEquals(mockList, result);
    }

    @Test
    void obtenerCoincidenciasPorHallazgo_shouldReturnList() {
        List<Map<String, Object>> mockList = new ArrayList<>();
        mockList.add(mockMap);
        when(coincidenciasClient.obtenerPorHallazgo(1L)).thenReturn(mockList);
        List<Map<String, Object>> result = service.obtenerCoincidenciasPorHallazgo(1L);
        assertEquals(mockList, result);
    }

    @Test
    void fallbackObtenerCoincidencias_shouldReturnEmptyList() {
        List<Map<String, Object>> result = service.fallbackObtenerCoincidencias(1L, new RuntimeException("Error"));
        assertEquals(0, result.size());
    }
}
