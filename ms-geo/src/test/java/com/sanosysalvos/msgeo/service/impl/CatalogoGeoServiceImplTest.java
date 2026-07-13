package com.sanosysalvos.msgeo.service.impl;

import com.sanosysalvos.msgeo.model.Comuna;
import com.sanosysalvos.msgeo.model.Region;
import com.sanosysalvos.msgeo.repository.ComunaRepository;
import com.sanosysalvos.msgeo.repository.RegionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogoGeoServiceImplTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private ComunaRepository comunaRepository;

    @InjectMocks
    private CatalogoGeoServiceImpl catalogoGeoService;

    @Test
    void obtenerRegiones_shouldReturnList() {
        Region region = new Region();
        region.setId(1L);
        region.setNombre("Metropolitana");

        when(regionRepository.findAll()).thenReturn(List.of(region));

        List<Region> result = catalogoGeoService.obtenerRegiones();
        assertEquals(1, result.size());
        assertEquals("Metropolitana", result.get(0).getNombre());
    }

    @Test
    void obtenerComunasPorRegion_shouldReturnList() {
        Comuna comuna = new Comuna();
        comuna.setId(1L);
        comuna.setNombre("Santiago");

        when(comunaRepository.findByRegionId(1L)).thenReturn(List.of(comuna));

        List<Comuna> result = catalogoGeoService.obtenerComunasPorRegion(1L);
        assertEquals(1, result.size());
        assertEquals("Santiago", result.get(0).getNombre());
    }
}
