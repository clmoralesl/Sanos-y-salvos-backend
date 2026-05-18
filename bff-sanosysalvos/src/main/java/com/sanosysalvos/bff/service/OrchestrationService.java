package com.sanosysalvos.bff.service;

import com.sanosysalvos.bff.client.GeoClient;
import com.sanosysalvos.bff.client.MascotasClient;
import com.sanosysalvos.bff.dto.ReporteDetalleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrchestrationService {

    private final MascotasClient mascotasClient;
    private final GeoClient geoClient;

    public ReporteDetalleDTO obtenerDetalleCompleto(Long idReporte, String auth0Id) {
        try {
            Map<String, Object> reporte = mascotasClient.obtenerReporte(idReporte, auth0Id);
            
            Long idMascota = parseLong(reporte.get("idMascota"));
            Long idUbicacion = parseLong(reporte.get("idUbicacionReporte"));
            Long idUsuario = parseLong(reporte.get("idUsuario"));

            Map<String, Object> mascota = mascotasClient.obtenerMascota(idMascota);
            Map<String, Object> ubicacion = geoClient.obtenerUbicacion(idUbicacion);
            Map<String, Object> usuario = mascotasClient.obtenerUsuario(idUsuario);

            return ReporteDetalleDTO.builder()
                .idReporte(idReporte)
                .fechaRegistro(String.valueOf(reporte.get("fechaRegistro")))
                .fechaIncidente(String.valueOf(reporte.get("fechaIncidente")))
                .tipoReporte(String.valueOf(reporte.get("tipoReporte")))
                .estadoReporte(String.valueOf(reporte.get("estadoReporte")))
                .idMascota(idMascota)
                .idUbicacionReporte(idUbicacion)
                .usuario(ReporteDetalleDTO.UsuarioDetalle.builder()
                        .nombre(String.valueOf(usuario.get("nombre")))
                        .email(String.valueOf(usuario.get("email")))
                        .telefono(usuario.get("telefono") != null ? usuario.get("telefono").toString() : "No disponible")
                        .build())
                .mascota(ReporteDetalleDTO.MascotaDetalle.builder()
                        .id(idMascota)
                        .nombre(String.valueOf(mascota.get("nombreMascota")))
                        .descripcion(String.valueOf(mascota.get("descripcion")))
                        .raza(String.valueOf(mascota.get("nombreRaza")))
                        .especie(mascota.get("especieRaza") != null ? mascota.get("especieRaza").toString() : "N/A")
                        .tamanio(String.valueOf(mascota.get("descripcionTamanio")))
                        .fotos(parseList(mascota.get("urlsFotografias")))
                        .build())
                .ubicacion(ReporteDetalleDTO.UbicacionDetalle.builder()
                        .id(idUbicacion)
                        .latitud(parseDouble(ubicacion.get("latitud")))
                        .longitud(parseDouble(ubicacion.get("longitud")))
                        .comuna(extractNestedString(ubicacion, "comuna", "nombre"))
                        .region(extractNestedString(ubicacion, "comuna", "region", "nombre_region"))
                        .codigoH3(extractNestedString(ubicacion, "zonaGeo", "id"))
                        .build())
                .build();

        } catch (Exception e) {
            log.error("Error orquestación: {}", e.getMessage());
            throw new RuntimeException("Error al orquestar: " + e.getMessage());
        }
    }

    private Long parseLong(Object value) {
        if (value == null) return null;
        try {
            return Long.valueOf(value.toString());
        } catch (Exception e) { return null; }
    }

    private Double parseDouble(Object value) {
        if (value == null) return 0.0;
        try {
            return Double.valueOf(value.toString());
        } catch (Exception e) { return 0.0; }
    }

    private List<String> parseList(Object value) {
        if (value instanceof List) return (List<String>) value;
        return new ArrayList<>();
    }

    private String extractNestedString(Map<String, Object> map, String... keys) {
        Object current = map;
        for (String key : keys) {
            if (!(current instanceof Map)) return "N/A";
            current = ((Map<String, Object>) current).get(key);
            if (current == null) return "N/A";
        }
        return current.toString();
    }
}
