package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.entity.EstadoReporte;
import com.sanosysalvos.mascotas.entity.Mascota;
import com.sanosysalvos.mascotas.entity.Reporte;
import com.sanosysalvos.mascotas.entity.TipoReporte;
import com.sanosysalvos.mascotas.entity.Usuario;
import com.sanosysalvos.mascotas.repository.EstadoReporteRepository;
import com.sanosysalvos.mascotas.repository.TipoReporteRepository;
import com.sanosysalvos.mascotas.service.ReporteFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReporteFactoryImpl implements ReporteFactory {

    private final TipoReporteRepository tipoReporteRepository;
    private final EstadoReporteRepository estadoReporteRepository;

    @Override
    public Reporte crearReporte(ReporteRequestDTO request, Usuario usuario, Mascota mascota) {
        TipoReporte tipoReporte = tipoReporteRepository.findById(request.getIdTipoReporte())
                .orElseThrow(() -> new RuntimeException("Tipo de reporte no encontrado"));

        EstadoReporte estadoActivo = estadoReporteRepository.findByDescripcion("Activo");
        if (estadoActivo == null) {
            estadoActivo = estadoReporteRepository.findById(1L).orElseThrow(() -> new RuntimeException("Estado Activo no encontrado"));
        }

        // Aquí aplicamos el núcleo del Factory Method: 
        // Si más adelante un Reporte de Hallazgo requiere propiedades distintas a uno de Pérdida,
        // este es el lugar para bifurcar la lógica de creación.
        return Reporte.builder()
                .fechaIncidente(request.getFechaIncidente())
                .idUbicacionReporte(request.getIdUbicacionReporte())
                .tipoReporte(tipoReporte)
                .estadoReporte(estadoActivo)
                .usuario(usuario)
                .mascota(mascota)
                .build();
    }

    @Override
    public ReporteResponseDTO mapearAResponse(Reporte reporte) {
        return ReporteResponseDTO.builder()
                .idReporte(reporte.getIdReporte())
                .fechaRegistro(reporte.getFechaRegistro())
                .fechaIncidente(reporte.getFechaIncidente())
                .idUbicacionReporte(reporte.getIdUbicacionReporte())
                .tipoReporte(reporte.getTipoReporte() != null ? reporte.getTipoReporte().getDescripcion() : null)
                .estadoReporte(reporte.getEstadoReporte() != null ? reporte.getEstadoReporte().getDescripcion() : null)
                .idUsuario(reporte.getUsuario() != null ? reporte.getUsuario().getIdUsuario() : null)
                .nombreUsuario(reporte.getUsuario() != null ? reporte.getUsuario().getNombre() : null)
                .idMascota(reporte.getMascota() != null ? reporte.getMascota().getIdMascota() : null)
                .nombreMascota(reporte.getMascota() != null ? reporte.getMascota().getNombreMascota() : null)
                .especieMascota(reporte.getMascota() != null && reporte.getMascota().getRaza() != null && reporte.getMascota().getRaza().getEspecie() != null 
                        ? reporte.getMascota().getRaza().getEspecie().getNombreEspecie() : null)
                .build();
    }
}
