package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.repository.*;
import com.sanosysalvos.mascotas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final TipoReporteRepository tipoReporteRepository;
    private final EstadoReporteRepository estadoReporteRepository;
    private final UsuarioRepository usuarioRepository;
    private final MascotaRepository mascotaRepository;

    @Override
    @Transactional
    public ReporteResponseDTO crearReporte(ReporteRequestDTO request, String auth0Id) {
        Usuario usuario = usuarioRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = mascotaRepository.findById(request.getIdMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        TipoReporte tipoReporte = tipoReporteRepository.findById(request.getIdTipoReporte())
                .orElseThrow(() -> new RuntimeException("Tipo de reporte no encontrado"));

        // Por defecto el estado inicial es "Activo" o id=1
        EstadoReporte estadoReporte = estadoReporteRepository.findByDescripcion("Activo");
        if (estadoReporte == null) {
            estadoReporte = estadoReporteRepository.findById(1L).orElseThrow(() -> new RuntimeException("Estado de reporte no encontrado"));
        }

        Reporte reporte = Reporte.builder()
                .fechaReporte(LocalDateTime.now())
                .idUbicacionReporte(request.getIdUbicacionReporte())
                .tipoReporte(tipoReporte)
                .estadoReporte(estadoReporte)
                .usuario(usuario)
                .mascota(mascota)
                .build();

        reporte = reporteRepository.save(reporte);

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

        // Verificar si ya está cerrado para dar un feedback claro
        if (reporte.getEstadoReporte() != null && 
           (reporte.getEstadoReporte().getDescripcion().equalsIgnoreCase("Cerrado") || 
            reporte.getEstadoReporte().getDescripcion().equalsIgnoreCase("Cerrado/Resuelto"))) {
            throw new RuntimeException("El reporte ya se encontraba cerrado previamente");
        }

        EstadoReporte estadoCerrado = estadoReporteRepository.findByDescripcion("Cerrado/Resuelto");
        if(estadoCerrado != null) {
            reporte.setEstadoReporte(estadoCerrado);
        } else {
             // Fallback
             reporte.setEstadoReporte(estadoReporteRepository.findById(2L).orElseThrow(() -> new RuntimeException("Estado Cerrado no encontrado")));
        }
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
        // Al actualizar no cambiamos fechaReporte ni el estado implícitamente, ni el usuario.

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
                .fechaReporte(reporte.getFechaReporte())
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