package com.sanosysalvos.msgeo.service.impl;

import com.sanosysalvos.msgeo.dto.UbicacionRequestDTO;
import com.sanosysalvos.msgeo.model.Comuna;
import com.sanosysalvos.msgeo.model.UbicacionReporte;
import com.sanosysalvos.msgeo.model.ZonaGeo;
import com.sanosysalvos.msgeo.repository.ComunaRepository;
import com.sanosysalvos.msgeo.repository.UbicacionReporteRepository;
import com.sanosysalvos.msgeo.repository.ZonaGeoRepository;
import com.sanosysalvos.msgeo.service.UbicacionService;
import com.uber.h3core.H3Core;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionReporteRepository ubicacionReporteRepository;
    private final ComunaRepository comunaRepository;
    private final ZonaGeoRepository zonaGeoRepository;

    @Value("${h3.resolution.default:8}")
    private int defaultResolution;

    @Override
    @Transactional
    public Long crearUbicacion(UbicacionRequestDTO request) {
        Comuna comuna = comunaRepository.findById(request.getIdComuna())
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));

        String h3Index = calcularIndiceH3(request.getLatitud(), request.getLongitud(), defaultResolution);

        ZonaGeo zonaGeo = zonaGeoRepository.findById(h3Index)
                .orElseGet(() -> zonaGeoRepository.save(ZonaGeo.builder().id(h3Index).build()));

        UbicacionReporte ubicacion = UbicacionReporte.builder()
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .comuna(comuna)
                .zonaGeo(zonaGeo)
                .build();

        ubicacion = ubicacionReporteRepository.save(ubicacion);

        return ubicacion.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUbicacion(Long id) {
        return ubicacionReporteRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UbicacionReporte obtenerUbicacionPorId(Long id) {
        return ubicacionReporteRepository.findById(id).orElse(null);
    }

    private String calcularIndiceH3(double lat, double lng, int resolucion) {
        try {
            H3Core h3 = H3Core.newInstance();
            return h3.latLngToCellAddress(lat, lng, resolucion);
        } catch (IOException e) {
            throw new RuntimeException("Error inicializando H3Core", e);
        }
    }
}

