package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.CatalogoResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogoServiceImplTest {

    @Mock
    private RazaRepository razaRepository;
    @Mock
    private TamanioRepository tamanioRepository;
    @Mock
    private CaracteristicaRepository caracteristicaRepository;
    @Mock
    private TipoReporteRepository tipoReporteRepository;
    @Mock
    private TipoCuentaRepository tipoCuentaRepository;
    @Mock
    private EspecieRepository especieRepository;

    @InjectMocks
    private CatalogoServiceImpl catalogoService;

    @Test
    void obtenerRazas_shouldReturnList() {
        Raza r = new Raza();
        r.setIdRaza(1L);
        r.setNombreRaza("Pastor Aleman");
        Especie e = new Especie();
        e.setIdEspecie(2L);
        r.setEspecie(e);

        when(razaRepository.findAll()).thenReturn(List.of(r));

        List<CatalogoResponseDTO> res = catalogoService.obtenerRazas();
        assertEquals(1, res.size());
        assertEquals(1L, res.get(0).getId());
        assertEquals("Pastor Aleman", res.get(0).getDescripcion());
        assertEquals(2L, res.get(0).getIdEspecie());
    }

    @Test
    void obtenerTamanios_shouldReturnList() {
        Tamanio t = new Tamanio();
        t.setIdTamanio(1L);
        t.setDescripcionTamanio("Grande");

        when(tamanioRepository.findAll()).thenReturn(List.of(t));

        List<CatalogoResponseDTO> res = catalogoService.obtenerTamanios();
        assertEquals(1, res.size());
        assertEquals(1L, res.get(0).getId());
    }

    @Test
    void obtenerCaracteristicas_shouldReturnList() {
        Caracteristica c = new Caracteristica();
        c.setIdCaracteristica(1L);
        c.setDescripcion("Amigable");

        when(caracteristicaRepository.findAll()).thenReturn(List.of(c));

        List<CatalogoResponseDTO> res = catalogoService.obtenerCaracteristicas();
        assertEquals(1, res.size());
        assertEquals(1L, res.get(0).getId());
    }

    @Test
    void obtenerTiposReporte_shouldReturnList() {
        TipoReporte tr = new TipoReporte();
        tr.setIdTipoReporte(1L);
        tr.setDescripcion("Perdido");

        when(tipoReporteRepository.findAll()).thenReturn(List.of(tr));

        List<CatalogoResponseDTO> res = catalogoService.obtenerTiposReporte();
        assertEquals(1, res.size());
        assertEquals(1L, res.get(0).getId());
    }

    @Test
    void obtenerTiposCuenta_shouldReturnList() {
        TipoCuenta tc = new TipoCuenta();
        tc.setIdTipoCuenta(1L);
        tc.setDescripcion("Premium");

        when(tipoCuentaRepository.findAll()).thenReturn(List.of(tc));

        List<CatalogoResponseDTO> res = catalogoService.obtenerTiposCuenta();
        assertEquals(1, res.size());
        assertEquals(1L, res.get(0).getId());
    }

    @Test
    void obtenerEspecies_shouldReturnList() {
        Especie e = new Especie();
        e.setIdEspecie(1L);
        e.setNombreEspecie("Perro");

        when(especieRepository.findAll()).thenReturn(List.of(e));

        List<CatalogoResponseDTO> res = catalogoService.obtenerEspecies();
        assertEquals(1, res.size());
        assertEquals(1L, res.get(0).getId());
    }
}
