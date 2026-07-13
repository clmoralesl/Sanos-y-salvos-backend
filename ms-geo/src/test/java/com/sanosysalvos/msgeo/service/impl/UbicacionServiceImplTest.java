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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UbicacionServiceImplTest {

    @Mock
    private UbicacionReporteRepository ubicacionReporteRepository;

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private ZonaGeoRepository zonaGeoRepository;

    @InjectMocks
    private UbicacionServiceImpl ubicacionService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(ubicacionService, "defaultResolution", 8);
    }

    @Test
    void crearUbicacion_shouldCreateAndReturnId() {
        UbicacionRequestDTO request = new UbicacionRequestDTO();
        request.setIdComuna(1L);
        request.setLatitud(-33.4489);
        request.setLongitud(-70.6693);
        request.setDireccionEspecifica("Av. Libertador Bernardo O'Higgins 123");

        Comuna comuna = Comuna.builder().id(1L).nombre("Santiago").build();
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comuna));

        ZonaGeo zonaGeo = ZonaGeo.builder().id("88a649a463fffff").build();
        when(zonaGeoRepository.findById(anyString())).thenReturn(Optional.empty());
        when(zonaGeoRepository.save(any(ZonaGeo.class))).thenReturn(zonaGeo);

        UbicacionReporte savedUbicacion = UbicacionReporte.builder()
                .id(100L)
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .build();
        when(ubicacionReporteRepository.save(any(UbicacionReporte.class))).thenReturn(savedUbicacion);

        Long id = ubicacionService.crearUbicacion(request);

        assertNotNull(id);
        assertEquals(100L, id);
        verify(comunaRepository).findById(1L);
        verify(ubicacionReporteRepository).save(any(UbicacionReporte.class));
    }

    @Test
    void existeUbicacion_shouldReturnTrue() {
        when(ubicacionReporteRepository.existsById(1L)).thenReturn(true);
        assertTrue(ubicacionService.existeUbicacion(1L));
    }

    @Test
    void obtenerUbicacionPorId_shouldReturnUbicacion() {
        UbicacionReporte ubicacion = UbicacionReporte.builder().id(1L).build();
        when(ubicacionReporteRepository.findById(1L)).thenReturn(Optional.of(ubicacion));

        UbicacionReporte result = ubicacionService.obtenerUbicacionPorId(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerUbicacionesEnRadio_shouldReturnList() {
        UbicacionReporte base = UbicacionReporte.builder()
                .id(1L)
                .zonaGeo(ZonaGeo.builder().id("88a649a463fffff").build())
                .build();
        when(ubicacionReporteRepository.findById(1L)).thenReturn(Optional.of(base));

        UbicacionReporte result1 = UbicacionReporte.builder().id(2L).build();
        UbicacionReporte result2 = UbicacionReporte.builder().id(3L).build();

        when(ubicacionReporteRepository.findByZonaGeo_IdIn(anyList())).thenReturn(List.of(result1, result2));

        List<Long> ids = ubicacionService.obtenerUbicacionesEnRadio(1L, 1);

        assertNotNull(ids);
        assertEquals(2, ids.size());
        assertTrue(ids.contains(2L));
        assertTrue(ids.contains(3L));
    }
}
