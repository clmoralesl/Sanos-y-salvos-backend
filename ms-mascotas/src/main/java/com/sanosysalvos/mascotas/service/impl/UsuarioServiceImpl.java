package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.UsuarioRequestDTO;
import com.sanosysalvos.mascotas.dto.UsuarioResponseDTO;
import com.sanosysalvos.mascotas.entity.Organizacion;
import com.sanosysalvos.mascotas.entity.TipoCuenta;
import com.sanosysalvos.mascotas.entity.Usuario;
import com.sanosysalvos.mascotas.exception.ResourceNotFoundException;
import com.sanosysalvos.mascotas.repository.OrganizacionRepository;
import com.sanosysalvos.mascotas.repository.TipoCuentaRepository;
import com.sanosysalvos.mascotas.repository.UsuarioRepository;
import com.sanosysalvos.mascotas.service.RabbitMQProducer;
import com.sanosysalvos.mascotas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoCuentaRepository tipoCuentaRepository;
    private final OrganizacionRepository organizacionRepository;
    private final RabbitMQProducer rabbitMQProducer;

    @Override
    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO request) {
        
        if (usuarioRepository.existsByAuth0Id(request.getAuth0Id())) {
            throw new RuntimeException("El usuario con Auth0 ID " + request.getAuth0Id() + " ya está registrado.");
        }

        Long idTipoCuenta = (request.getIdTipoCuenta() != null) ? request.getIdTipoCuenta() : 1L;
        TipoCuenta tipoCuenta = tipoCuentaRepository.findById(idTipoCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cuenta no encontrado con ID: " + idTipoCuenta));

        Organizacion organizacion = null;
        String estadoMembresia = "NINGUNO";
        
        if (request.getIdOrganizacion() != null) {
            organizacion = organizacionRepository.findById(request.getIdOrganizacion())
                    .orElseThrow(() -> new ResourceNotFoundException("Organización no encontrada con ID: " + request.getIdOrganizacion()));
            estadoMembresia = "PENDIENTE";
        }

        Usuario nuevoUsuario = Usuario.builder()
                .auth0Id(request.getAuth0Id())
                .nombre(request.getNombre())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .tipoCuenta(tipoCuenta)
                .organizacion(organizacion)
                .estadoMembresia(estadoMembresia)
                .build();

        Usuario guardado = usuarioRepository.save(nuevoUsuario);

        return mapToDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPerfilPorAuth0Id(String auth0Id) {
        Usuario usuario = usuarioRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un perfil para el Auth0 ID proporcionado."));

        return mapToDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(String auth0Id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un perfil para el Auth0 ID proporcionado."));

        Long idTipoCuenta = (request.getIdTipoCuenta() != null) ? request.getIdTipoCuenta() : 1L;
        TipoCuenta tipoCuenta = tipoCuentaRepository.findById(idTipoCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cuenta no encontrado con ID: " + idTipoCuenta));

        Organizacion organizacion = null;
        if (request.getIdOrganizacion() != null) {
            organizacion = organizacionRepository.findById(request.getIdOrganizacion())
                    .orElseThrow(() -> new ResourceNotFoundException("Organización no encontrada con ID: " + request.getIdOrganizacion()));
            
            if (usuario.getOrganizacion() == null || !usuario.getOrganizacion().getIdOrganizacion().equals(organizacion.getIdOrganizacion())) {
                usuario.setEstadoMembresia("PENDIENTE");
                
                // Find ADMIN_ORG of this organization to notify them
                final Long targetOrgId = organizacion.getIdOrganizacion();
                Optional<Usuario> admin = usuarioRepository.findAll().stream()
                        .filter(u -> u.getOrganizacion() != null 
                                && u.getOrganizacion().getIdOrganizacion().equals(targetOrgId) 
                                && u.getTipoCuenta() != null 
                                && u.getTipoCuenta().getIdTipoCuenta() == 2L)
                        .findFirst();
                        
                if (admin.isPresent()) {
                    rabbitMQProducer.enviarNotificacion(
                            admin.get().getIdUsuario(),
                            "Nueva Solicitud de Voluntario",
                            "El usuario " + request.getNombre() + " ha solicitado unirse a tu organización.",
                            "ALERTA"
                    );
                }
            }
        } else {
            usuario.setEstadoMembresia("NINGUNO");
        }

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setTipoCuenta(tipoCuenta);
        usuario.setOrganizacion(organizacion);

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToDTO(guardado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(String auth0Id) {
        Usuario usuario = usuarioRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró un perfil para el Auth0 ID proporcionado."));
        usuarioRepository.delete(usuario);
    }


    @Override
    @Transactional(readOnly = true)
    public java.util.List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró usuario con el ID: " + id));
        return mapToDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuarioPorId(Long id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró usuario con el ID: " + id));

        Long idTipoCuenta = (request.getIdTipoCuenta() != null) ? request.getIdTipoCuenta() : 1L;
        TipoCuenta tipoCuenta = tipoCuentaRepository.findById(idTipoCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cuenta no encontrado con ID: " + idTipoCuenta));

        Organizacion organizacion = null;
        if (request.getIdOrganizacion() != null) {
            organizacion = organizacionRepository.findById(request.getIdOrganizacion())
                    .orElseThrow(() -> new ResourceNotFoundException("Organización no encontrada con ID: " + request.getIdOrganizacion()));
            
            if (usuario.getOrganizacion() == null || !usuario.getOrganizacion().getIdOrganizacion().equals(organizacion.getIdOrganizacion())) {
                usuario.setEstadoMembresia("PENDIENTE");
            }
        } else {
            usuario.setEstadoMembresia("NINGUNO");
        }

        usuario.setAuth0Id(request.getAuth0Id());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setTipoCuenta(tipoCuenta);
        usuario.setOrganizacion(organizacion);

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToDTO(guardado);
    }

    @Override
    @Transactional
    public void eliminarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró usuario con el ID: " + id));
        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarMembresia(Long idUsuario, String estado) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró usuario con el ID: " + idUsuario));
        
        usuario.setEstadoMembresia(estado);
        Usuario guardado = usuarioRepository.save(usuario);
        
        String titulo = estado.equals("APROBADO") ? "Membresía Aprobada" : "Membresía Rechazada";
        String mensaje = estado.equals("APROBADO") 
                ? "¡Felicidades! Tu solicitud para unirte a la organización ha sido aprobada." 
                : "Lo sentimos, tu solicitud para unirte a la organización ha sido rechazada o revocada.";
                
        rabbitMQProducer.enviarNotificacion(usuario.getIdUsuario(), titulo, mensaje, "SISTEMA");
        
        return mapToDTO(guardado);
    }
    
    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        UsuarioResponseDTO dto = UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .auth0Id(usuario.getAuth0Id())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .descripcionTipoCuenta(usuario.getTipoCuenta() != null ? usuario.getTipoCuenta().getDescripcion() : null)
                .estadoMembresia(usuario.getEstadoMembresia())
                .build();
        
        if (usuario.getOrganizacion() != null) {
            dto.setIdOrganizacion(usuario.getOrganizacion().getIdOrganizacion());
            dto.setNombreOrganizacion(usuario.getOrganizacion().getNombreOrganizacion());
            dto.setEstadoOrganizacion(usuario.getOrganizacion().getEstado());
        }
        
        return dto;
    }
}
