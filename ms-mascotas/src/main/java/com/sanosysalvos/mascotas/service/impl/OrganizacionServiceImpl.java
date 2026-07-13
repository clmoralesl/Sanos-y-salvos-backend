package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.OrganizacionRequestDTO;
import com.sanosysalvos.mascotas.dto.OrganizacionResponseDTO;
import com.sanosysalvos.mascotas.entity.Organizacion;
import com.sanosysalvos.mascotas.repository.OrganizacionRepository;
import com.sanosysalvos.mascotas.service.OrganizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizacionServiceImpl implements OrganizacionService {
    private final OrganizacionRepository organizacionRepository;
    private final com.sanosysalvos.mascotas.repository.UsuarioRepository usuarioRepository;

    @Override
    public OrganizacionResponseDTO crearOrganizacion(OrganizacionRequestDTO request) {
        Organizacion organizacion = Organizacion.builder()
                .nombreOrganizacion(request.getNombreOrganizacion())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .rut(request.getRut())
                .rutRepresentante(request.getRutRepresentante())
                .estado("PENDIENTE") // Default to PENDIENTE
                .build();

        Organizacion guardada = organizacionRepository.save(organizacion);
        return mapToDTO(guardada);
    }

    @Override
    public OrganizacionResponseDTO obtenerOrganizacionPorId(Long id) {
        Organizacion organizacion = organizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada con ID: " + id));
        return mapToDTO(organizacion);
    }

    @Override
    public List<OrganizacionResponseDTO> obtenerTodas() {
        return organizacionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizacionResponseDTO actualizarOrganizacion(Long id, OrganizacionRequestDTO request) {
        Organizacion organizacion = organizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada con ID: " + id));

        organizacion.setNombreOrganizacion(request.getNombreOrganizacion());
        organizacion.setDireccion(request.getDireccion());
        organizacion.setTelefono(request.getTelefono());
        organizacion.setEmail(request.getEmail());
        organizacion.setRut(request.getRut());
        organizacion.setRutRepresentante(request.getRutRepresentante());

        Organizacion actualizada = organizacionRepository.save(organizacion);
        return mapToDTO(actualizada);
    }

    @Override
    public void eliminarOrganizacion(Long id) {
        Organizacion organizacion = organizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada con ID: " + id));
        organizacionRepository.delete(organizacion);
    }

    @Override
    public OrganizacionResponseDTO actualizarEstado(Long id, String estado) {
        Organizacion organizacion = organizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada con ID: " + id));
        organizacion.setEstado(estado);
        Organizacion actualizada = organizacionRepository.save(organizacion);

        if ("ACTIVA".equals(estado)) {
            java.util.List<com.sanosysalvos.mascotas.entity.Usuario> usuarios = usuarioRepository.findByOrganizacionIdOrganizacion(id);
            for (com.sanosysalvos.mascotas.entity.Usuario u : usuarios) {
                if (u.getTipoCuenta() != null && "ADMIN_ORG".equals(u.getTipoCuenta().getDescripcion())) {
                    u.setEstadoMembresia("APROBADO");
                    usuarioRepository.save(u);
                }
            }
        }

        return mapToDTO(actualizada);
    }

    private OrganizacionResponseDTO mapToDTO(Organizacion organizacion) {
        return OrganizacionResponseDTO.builder()
                .idOrganizacion(organizacion.getIdOrganizacion())
                .nombreOrganizacion(organizacion.getNombreOrganizacion())
                .direccion(organizacion.getDireccion())
                .telefono(organizacion.getTelefono())
                .email(organizacion.getEmail())
                .rut(organizacion.getRut())
                .rutRepresentante(organizacion.getRutRepresentante())
                .estado(organizacion.getEstado())
                .build();
    }
}
