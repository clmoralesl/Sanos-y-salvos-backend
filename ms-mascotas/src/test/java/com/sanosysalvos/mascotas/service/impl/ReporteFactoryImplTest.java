package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.repository.EstadoReporteRepository;
import com.sanosysalvos.mascotas.repository.TipoReporteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteFactoryImplTest {

    @Mock
    private TipoReporteRepository tipoReporteRepository;

    @Mock
    private EstadoReporteRepository estadoReporteRepository;

    @InjectMocks
    private ReporteFactoryImpl reporteFactory;

    @Test
    void crearReporte_shouldCreateReporte() {
        ReporteRequestDTO req = new ReporteRequestDTO();
        req.setIdTipoReporte(1L);

        Usuario user = new Usuario();
        user.setIdUsuario(10L);

        Mascota mascota = new Mascota();
        mascota.setIdMascota(20L);

        TipoReporte tr = new TipoReporte();
        tr.setIdTipoReporte(1L);

        EstadoReporte er = new EstadoReporte();
        er.setIdEstadoReporte(1L);

        when(tipoReporteRepository.findById(1L)).thenReturn(Optional.of(tr));
        when(estadoReporteRepository.findByDescripcion("Activo")).thenReturn(er);

        Reporte result = reporteFactory.crearReporte(req, user, mascota);

        assertNotNull(result);
        assertEquals(tr, result.getTipoReporte());
        assertEquals(er, result.getEstadoReporte());
        assertEquals(user, result.getUsuario());
        assertEquals(mascota, result.getMascota());
    }

    @Test
    void mapearAResponse_shouldReturnDto() {
        Reporte reporte = new Reporte();
        reporte.setIdReporte(1L);
        TipoReporte tr = new TipoReporte();
        tr.setDescripcion("Perdido");
        reporte.setTipoReporte(tr);

        Usuario user = new Usuario();
        user.setIdUsuario(10L);
        user.setNombre("Claudio");
        reporte.setUsuario(user);

        Mascota mascota = new Mascota();
        mascota.setIdMascota(20L);
        mascota.setNombreMascota("Firulais");
        reporte.setMascota(mascota);

        ReporteResponseDTO dto = reporteFactory.mapearAResponse(reporte);

        assertNotNull(dto);
        assertEquals(1L, dto.getIdReporte());
        assertEquals("Perdido", dto.getTipoReporte());
        assertEquals("Claudio", dto.getNombreUsuario());
        assertEquals("Firulais", dto.getNombreMascota());
    }
}
