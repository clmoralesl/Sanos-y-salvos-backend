package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.client.CoincidenciaClient;
import com.sanosysalvos.mascotas.client.UbicacionClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MascotasIntegrationServiceTest {

    @Mock
    private UbicacionClient ubicacionClient;

    @Mock
    private CoincidenciaClient coincidenciaClient;

    @InjectMocks
    private MascotasIntegrationService mascotasIntegrationService;

    @Test
    void testObtenerUbicacion_Exitoso() {
        Long idUbicacion = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(ubicacionClient.obtenerUbicacion(idUbicacion)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = mascotasIntegrationService.obtenerUbicacion(idUbicacion);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ubicacionClient).obtenerUbicacion(idUbicacion);
    }

    @Test
    void testFallbackObtenerUbicacion() {
        Long idUbicacion = 1L;
        Throwable exception = new RuntimeException("Test Exception");

        ResponseEntity<Object> response = mascotasIntegrationService.fallbackObtenerUbicacion(idUbicacion, exception);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, ((Map<?, ?>) response.getBody()).size());
    }

    @Test
    void testProcesarReporteTrigger_Exitoso() {
        Long idReporte = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(coincidenciaClient.procesarReporteTrigger(idReporte)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = mascotasIntegrationService.procesarReporteTrigger(idReporte);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(coincidenciaClient).procesarReporteTrigger(idReporte);
    }

    @Test
    void testFallbackProcesarReporteTrigger() {
        Long idReporte = 1L;
        Throwable exception = new RuntimeException("Test Exception");

        ResponseEntity<Object> response = mascotasIntegrationService.fallbackProcesarReporteTrigger(idReporte, exception);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
