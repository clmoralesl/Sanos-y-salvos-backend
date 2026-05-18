package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.repository.*;
import com.sanosysalvos.mascotas.service.ReporteService;
import com.sanosysalvos.mascotas.client.CoincidenciaClient;
import com.sanosysalvos.mascotas.client.UbicacionClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final TipoReporteRepository tipoReporteRepository;
    private final EstadoReporteRepository estadoReporteRepository;
    private final UsuarioRepository usuarioRepository;
    private final MascotaRepository mascotaRepository;
    private final CoincidenciaClient coincidenciaClient;
    private final UbicacionClient ubicacionClient;

    @Override
    @Transactional
    public ReporteResponseDTO crearReporte(ReporteRequestDTO request, String auth0Id) {
        Usuario usuario = usuarioRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = mascotaRepository.findById(request.getIdMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        TipoReporte tipoReporte = tipoReporteRepository.findById(request.getIdTipoReporte())
                .orElseThrow(() -> new RuntimeException("Tipo de reporte no encontrado"));

        try {
            var respuesta = ubicacionClient.obtenerUbicacion(request.getIdUbicacionReporte());
            if (!respuesta.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("La ubicación especificada no existe en el sistema de geolocalización");
            }
        } catch (Exception e) {
            log.error("Error al validar ubicación en ms-geo: {}", e.getMessage());
            throw new RuntimeException("Rechazado: No se pudo validar la ubicación.");
        }

        EstadoReporte estadoActivo = estadoReporteRepository.findByDescripcion("Activo");
        if (estadoActivo == null) {
            estadoActivo = estadoReporteRepository.findById(1L).orElseThrow(() -> new RuntimeException("Estado Activo no encontrado"));
        }

        Reporte reporte = Reporte.builder()
                .fechaIncidente(request.getFechaIncidente())
                .idUbicacionReporte(request.getIdUbicacionReporte())
                .tipoReporte(tipoReporte)
                .estadoReporte(estadoActivo)
                .usuario(usuario)
                .mascota(mascota)
                .build();

        reporte = reporteRepository.save(reporte);

        try {
            coincidenciaClient.procesarReporteTrigger(reporte.getIdReporte());
        } catch (Exception e) {
            log.error("El ms-coincidencias falló: {}", e.getMessage());
        }

        return mapearAResponse(reporte);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> obtenerTodosLosReportes() {
        return reporteRepository.findAll().stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteResponseDTO obtenerReportePorId(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        return mapearAResponse(reporte);
    }

    @Override
    @Transactional
    public ReporteResponseDTO cerrarReporte(Long idReporte, String auth0Id) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        if (!reporte.getUsuario().getAuth0Id().equals(auth0Id)) {
            throw new RuntimeException("No tiene permisos para cerrar este reporte");
        }

        EstadoReporte estadoCerrado = estadoReporteRepository.findByDescripcion("Cerrado/Resuelto");
        if(estadoCerrado == null) {
             estadoCerrado = estadoReporteRepository.findById(2L).orElseThrow(() -> new RuntimeException("Estado Cerrado no encontrado"));
        }
        reporte.setEstadoReporte(estadoCerrado);
        reporte = reporteRepository.save(reporte);
        return mapearAResponse(reporte);
    }

    @Override
    @Transactional
    public ReporteResponseDTO actualizarReporte(Long idReporte, ReporteRequestDTO request, String auth0Id) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        if (!reporte.getUsuario().getAuth0Id().equals(auth0Id)) {
            throw new RuntimeException("No tiene permisos para modificar este reporte");
        }

        Mascota mascota = mascotaRepository.findById(request.getIdMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        TipoReporte tipoReporte = tipoReporteRepository.findById(request.getIdTipoReporte())
                .orElseThrow(() -> new RuntimeException("Tipo de reporte no encontrado"));

        reporte.setTipoReporte(tipoReporte);
        reporte.setMascota(mascota);
        reporte.setIdUbicacionReporte(request.getIdUbicacionReporte());
        reporte.setFechaIncidente(request.getFechaIncidente());

        Reporte reporteActualizado = reporteRepository.save(reporte);
        return mapearAResponse(reporteActualizado);
    }

    @Override
    @Transactional
    public void eliminarReporte(Long idReporte, String auth0Id) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        if (!reporte.getUsuario().getAuth0Id().equals(auth0Id)) {
            throw new RuntimeException("No tiene permisos para eliminar este reporte");
        }

        reporteRepository.delete(reporte);
    }

    private ReporteResponseDTO mapearAResponse(Reporte reporte) {
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
                .build();
    }
}
