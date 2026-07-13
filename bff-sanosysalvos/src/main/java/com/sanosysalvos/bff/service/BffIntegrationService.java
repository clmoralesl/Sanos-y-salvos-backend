package com.sanosysalvos.bff.service;

import com.sanosysalvos.bff.client.CoincidenciasClient;
import com.sanosysalvos.bff.client.GeoClient;
import com.sanosysalvos.bff.client.MascotasClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BffIntegrationService {

    private final MascotasClient mascotasClient;
    private final GeoClient geoClient;
    private final CoincidenciasClient coincidenciasClient;

    @CircuitBreaker(name = "bffMascotasCB", fallbackMethod = "fallbackObtenerReporte")
    public Map<String, Object> obtenerReporte(Long idReporte, String auth0Id) {
        return mascotasClient.obtenerReporte(idReporte, auth0Id);
    }

    public Map<String, Object> fallbackObtenerReporte(Long idReporte, String auth0Id, Throwable t) {
        log.error("Fallback activado para obtenerReporte (id: {}): {}", idReporte, t.getMessage());
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("idReporte", idReporte);
        fallbackResponse.put("estadoReporte", "Desconocido (Fallo de servicio)");
        return fallbackResponse;
    }

    @CircuitBreaker(name = "bffMascotasCB", fallbackMethod = "fallbackObtenerMascota")
    public Map<String, Object> obtenerMascota(Long idMascota) {
        return mascotasClient.obtenerMascota(idMascota);
    }

    public Map<String, Object> fallbackObtenerMascota(Long idMascota, Throwable t) {
        log.error("Fallback activado para obtenerMascota (id: {}): {}", idMascota, t.getMessage());
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("idMascota", idMascota);
        fallbackResponse.put("nombreMascota", "Datos de mascota no disponibles temporalmente");
        return fallbackResponse;
    }

    @CircuitBreaker(name = "bffGeoCB", fallbackMethod = "fallbackObtenerUbicacion")
    public Map<String, Object> obtenerUbicacion(Long idUbicacion) {
        return geoClient.obtenerUbicacion(idUbicacion);
    }

    public Map<String, Object> fallbackObtenerUbicacion(Long idUbicacion, Throwable t) {
        log.error("Fallback activado para obtenerUbicacion (id: {}): {}", idUbicacion, t.getMessage());
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("idUbicacion", idUbicacion);
        fallbackResponse.put("direccionEspecifica", "Ubicación temporalmente no disponible");
        return fallbackResponse;
    }

    @CircuitBreaker(name = "bffMascotasCB", fallbackMethod = "fallbackObtenerUsuario")
    public Map<String, Object> obtenerUsuario(Long idUsuario) {
        return mascotasClient.obtenerUsuario(idUsuario);
    }

    public Map<String, Object> fallbackObtenerUsuario(Long idUsuario, Throwable t) {
        log.error("Fallback activado para obtenerUsuario (id: {}): {}", idUsuario, t.getMessage());
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("idUsuario", idUsuario);
        fallbackResponse.put("nombre", "Usuario no disponible");
        return fallbackResponse;
    }

    @CircuitBreaker(name = "bffCoincidenciasCB", fallbackMethod = "fallbackObtenerCoincidencias")
    public List<Map<String, Object>> obtenerCoincidenciasPorPerdida(Long idReporte) {
        return coincidenciasClient.obtenerPorPerdida(idReporte);
    }

    @CircuitBreaker(name = "bffCoincidenciasCB", fallbackMethod = "fallbackObtenerCoincidencias")
    public List<Map<String, Object>> obtenerCoincidenciasPorHallazgo(Long idReporte) {
        return coincidenciasClient.obtenerPorHallazgo(idReporte);
    }

    public List<Map<String, Object>> fallbackObtenerCoincidencias(Long idReporte, Throwable t) {
        log.error("Fallback activado para obtenerCoincidencias (id: {}): {}", idReporte, t.getMessage());
        return new ArrayList<>();
    }
}
