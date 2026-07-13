package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.FiltroBusquedaMasivaDTO;
import com.sanosysalvos.mascotas.dto.ReporteRequestDTO;
import com.sanosysalvos.mascotas.dto.ReporteResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.repository.*;
import com.sanosysalvos.mascotas.service.ReporteFactory;
import com.sanosysalvos.mascotas.service.ReporteService;
import com.sanosysalvos.mascotas.service.MascotasIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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
    private final MascotasIntegrationService integrationService;
    private final ReporteFactory reporteFactory;

    @Override
    @Transactional
    public ReporteResponseDTO crearReporte(ReporteRequestDTO request, String auth0Id) {
        Usuario usuario = usuarioRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = mascotaRepository.findById(request.getIdMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        try {
            var respuesta = integrationService.obtenerUbicacion(request.getIdUbicacionReporte());
            if (respuesta != null && !respuesta.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("La ubicación especificada no existe en el sistema de geolocalización");
            }
        } catch (Exception e) {
            log.error("Error al validar ubicación en ms-geo: {}", e.getMessage());
            throw new RuntimeException("Rechazado: No se pudo validar la ubicación.");
        }

        Reporte reporte = reporteFactory.crearReporte(request, usuario, mascota);

        reporte = reporteRepository.save(reporte);

        final Long reporteId = reporte.getIdReporte();
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        integrationService.procesarReporteTrigger(reporteId);
                    } catch (Exception e) {
                        log.error("El ms-coincidencias fallo: {}", e.getMessage());
                    }
                }
            });
        } else {
            try {
                integrationService.procesarReporteTrigger(reporteId);
            } catch (Exception e) {
                log.error("El ms-coincidencias fallo: {}", e.getMessage());
            }
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

        final Long reporteId = reporteActualizado.getIdReporte();
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        integrationService.procesarReporteTrigger(reporteId);
                    } catch (Exception e) {
                        log.error("El ms-coincidencias fallo: {}", e.getMessage());
                    }
                }
            });
        } else {
            try {
                integrationService.procesarReporteTrigger(reporteId);
            } catch (Exception e) {
                log.error("El ms-coincidencias fallo: {}", e.getMessage());
            }
        }

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

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> buscarReportesCandidatos(FiltroBusquedaMasivaDTO filtro) {
        log.info("Buscando reportes candidatos para tipo: {}, especie: {} en {} ubicaciones", 
                filtro.getTipoReporteBuscado(), filtro.getEspecie(), filtro.getUbicacionesIds().size());
        
        return reporteRepository.findByEstadoReporte_DescripcionIgnoreCaseAndTipoReporte_DescripcionIgnoreCaseAndMascota_Raza_Especie_NombreEspecieIgnoreCaseAndIdUbicacionReporteIn(
                "Activo",
                filtro.getTipoReporteBuscado(),
                filtro.getEspecie(),
                filtro.getUbicacionesIds()
        ).stream()
        .map(this::mapearAResponse)
        .collect(Collectors.toList());
    }

    private ReporteResponseDTO mapearAResponse(Reporte reporte) {
        return reporteFactory.mapearAResponse(reporte);
    }
}
