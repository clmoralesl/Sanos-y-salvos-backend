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
    private final com.sanosysalvos.mascotas.service.RabbitMQProducer rabbitMQProducer;

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

        usuarioRepository.findAll().stream()
                .filter(u -> u.getTipoCuenta() != null && u.getTipoCuenta().getIdTipoCuenta() == 3L)
                .forEach(superAdmin -> rabbitMQProducer.enviarNotificacion(
                        superAdmin.getIdUsuario(),
                        "Nueva Solicitud de Organización",
                        "La organización '" + guardada.getNombreOrganizacion() + "' ha solicitado ser registrada en el sistema.",
                        "ALERTA",
                        "/admin/solicitudes-org"
                ));

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
                    
                    rabbitMQProducer.enviarNotificacion(
                            u.getIdUsuario(),
                            "Organización Aprobada",
                            "Felicidades, la organización '" + actualizada.getNombreOrganizacion() + "' ha sido aprobada.",
                            "SISTEMA",
                            "/admin/solicitudes-org"
                    );
                }
            }
        } else if ("RECHAZADA".equals(estado)) {
            java.util.List<com.sanosysalvos.mascotas.entity.Usuario> usuarios = usuarioRepository.findByOrganizacionIdOrganizacion(id);
            for (com.sanosysalvos.mascotas.entity.Usuario u : usuarios) {
                if (u.getTipoCuenta() != null && "ADMIN_ORG".equals(u.getTipoCuenta().getDescripcion())) {
                    rabbitMQProducer.enviarNotificacion(
                            u.getIdUsuario(),
                            "Organización Rechazada",
                            "Tu solicitud para registrar la organización '" + actualizada.getNombreOrganizacion() + "' fue denegada.",
                            "SISTEMA",
                            "/perfil"
                    );
                }
                
                u.setOrganizacion(null);
                u.setEstadoMembresia("NINGUNO");
                if (u.getTipoCuenta() != null && u.getTipoCuenta().getIdTipoCuenta() == 2L) {
                    com.sanosysalvos.mascotas.entity.TipoCuenta tipoEstandar = new com.sanosysalvos.mascotas.entity.TipoCuenta();
                    tipoEstandar.setIdTipoCuenta(1L);
                    u.setTipoCuenta(tipoEstandar);
                }
                usuarioRepository.save(u);
            }
        }

        return mapToDTO(actualizada);
    }

    private OrganizacionResponseDTO mapToDTO(Organizacion organizacion) {
        String repNombre = null;
        String repEmail = null;
        String repTelefono = null;

        if (organizacion.getIdOrganizacion() != null) {
            java.util.List<com.sanosysalvos.mascotas.entity.Usuario> usuarios = usuarioRepository.findByOrganizacionIdOrganizacion(organizacion.getIdOrganizacion());
            for (com.sanosysalvos.mascotas.entity.Usuario u : usuarios) {
                if (u.getTipoCuenta() != null && "ADMIN_ORG".equals(u.getTipoCuenta().getDescripcion())) {
                    repNombre = u.getNombre();
                    repEmail = u.getEmail();
                    repTelefono = u.getTelefono();
                    break;
                }
            }
        }

        return OrganizacionResponseDTO.builder()
                .idOrganizacion(organizacion.getIdOrganizacion())
                .nombreOrganizacion(organizacion.getNombreOrganizacion())
                .direccion(organizacion.getDireccion())
                .telefono(organizacion.getTelefono())
                .email(organizacion.getEmail())
                .rut(organizacion.getRut())
                .rutRepresentante(organizacion.getRutRepresentante())
                .nombreRepresentante(repNombre)
                .emailRepresentante(repEmail)
                .telefonoRepresentante(repTelefono)
                .estado(organizacion.getEstado())
                .build();
    }
}
