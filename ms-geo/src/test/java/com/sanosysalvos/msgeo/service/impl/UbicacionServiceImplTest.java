package com.sanosysalvos.msgeo.service.impl;

import com.sanosysalvos.msgeo.dto.UbicacionRequestDTO;
import com.sanosysalvos.msgeo.model.Comuna;
import com.sanosysalvos.msgeo.model.UbicacionReporte;
import com.sanosysalvos.msgeo.model.ZonaGeo;
import com.sanosysalvos.msgeo.repository.ComunaRepository;
import com.sanosysalvos.msgeo.repository.UbicacionReporteRepository;
import com.sanosysalvos.msgeo.repository.ZonaGeoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UbicacionServiceImplTest {

    @Mock
    private UbicacionReporteRepository ubicacionReporteRepository;

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private ZonaGeoRepository zonaGeoRepository;

    @InjectMocks
    private UbicacionServiceImpl service;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(service, "defaultResolution", 8);
    }

    @Test
    public void testCrearUbicacionComunaNotFound() {
        UbicacionRequestDTO request = new UbicacionRequestDTO();
        request.setIdComuna(999L);
        when(comunaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.crearUbicacion(request));
    }

    @Test
    public void testCrearUbicacionSuccess() {
        UbicacionRequestDTO request = new UbicacionRequestDTO();
        request.setIdComuna(1L);
        request.setLatitud(-33.4372);
        request.setLongitud(-70.6506);
        request.setDireccionEspecifica("Plaza de Armas");

        Comuna comuna = Comuna.builder().id(1L).nombre("Santiago").build();
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comuna));
        when(zonaGeoRepository.findById(anyString())).thenReturn(Optional.empty());
        when(zonaGeoRepository.save(any(ZonaGeo.class))).thenAnswer(i -> i.getArguments()[0]);

        UbicacionReporte saved = UbicacionReporte.builder()
                .id(100L)
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .direccionEspecifica(request.getDireccionEspecifica())
                .comuna(comuna)
                .build();
        when(ubicacionReporteRepository.save(any(UbicacionReporte.class))).thenReturn(saved);

        Long result = service.crearUbicacion(request);

        assertNotNull(result);
        assertEquals(100L, result);
        verify(ubicacionReporteRepository, times(1)).save(any(UbicacionReporte.class));
    }
}
