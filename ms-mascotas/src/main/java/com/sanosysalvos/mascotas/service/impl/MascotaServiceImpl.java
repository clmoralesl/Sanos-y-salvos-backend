package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.MascotaRequestDTO;
import com.sanosysalvos.mascotas.dto.MascotaResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.exception.ResourceNotFoundException;
import com.sanosysalvos.mascotas.repository.CaracteristicaRepository;
import com.sanosysalvos.mascotas.repository.MascotaRepository;
import com.sanosysalvos.mascotas.repository.RazaRepository;
import com.sanosysalvos.mascotas.repository.TamanioRepository;
import com.sanosysalvos.mascotas.repository.UsuarioRepository;
import com.sanosysalvos.mascotas.service.MascotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository mascotaRepository;
    private final RazaRepository razaRepository;
    private final TamanioRepository tamanioRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public MascotaResponseDTO createMascota(MascotaRequestDTO request, String auth0Id) {
        Usuario usuario = null;
        if (request.getSinDueno() == null || !request.getSinDueno()) {
            usuario = usuarioRepository.findByAuth0Id(auth0Id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con Auth0Id: " + auth0Id));
        }

        Raza raza = razaRepository.findById(request.getIdRaza())
                .orElseThrow(() -> new ResourceNotFoundException("Raza no encontrada con ID: " + request.getIdRaza()));

        Tamanio tamanio = tamanioRepository.findById(request.getIdTamanio())
                .orElseThrow(() -> new ResourceNotFoundException("Tamaño no encontrado con ID: " + request.getIdTamanio()));

        List<Caracteristica> caracteristicas = new ArrayList<>();
        if (request.getIdsCaracteristicas() != null && !request.getIdsCaracteristicas().isEmpty()) {
            caracteristicas = caracteristicaRepository.findAllById(request.getIdsCaracteristicas());
        }

        Mascota mascota = Mascota.builder()
                .nombreMascota(request.getNombreMascota())
                .descripcion(request.getDescripcion())
                .colorPrimario(request.getColorPrimario())
                .colorSecundario(request.getColorSecundario())
                .raza(raza)
                .tamanio(tamanio)
                .usuario(usuario)
                .caracteristicas(caracteristicas)
                .edadAproximada(request.getEdadAproximada())
                .build();

        if (request.getUrlsFotografias() != null && !request.getUrlsFotografias().isEmpty()) {
            List<Fotografia> fotografias = request.getUrlsFotografias().stream()
                    .map(url -> Fotografia.builder()
                            .urlFotografia(url)
                            .mascota(mascota)
                            .build())
                    .collect(Collectors.toList());
            mascota.setFotografias(fotografias);
        }

        Mascota mascotaGuardada = mascotaRepository.save(mascota);
        return mapToResponseDTO(mascotaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public MascotaResponseDTO getMascotaById(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con id: " + id));
        return mapToResponseDTO(mascota);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> getAllMascotas() {
        return mascotaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> getMisMascotas(String auth0Id) {
        return mascotaRepository.findByUsuarioAuth0Id(auth0Id).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MascotaResponseDTO updateMascota(Long id, MascotaRequestDTO request, String auth0Id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con id: " + id));

        if (mascota.getUsuario() != null && !mascota.getUsuario().getAuth0Id().equals(auth0Id)) {
             throw new RuntimeException("No tienes permisos para editar esta mascota");
        }

        Raza raza = razaRepository.findById(request.getIdRaza())
                .orElseThrow(() -> new ResourceNotFoundException("Raza no encontrada con ID: " + request.getIdRaza()));

        Tamanio tamanio = tamanioRepository.findById(request.getIdTamanio())
                .orElseThrow(() -> new ResourceNotFoundException("Tamaño no encontrado con ID: " + request.getIdTamanio()));

        List<Caracteristica> caracteristicas = new ArrayList<>();
        if (request.getIdsCaracteristicas() != null && !request.getIdsCaracteristicas().isEmpty()) {
            caracteristicas = caracteristicaRepository.findAllById(request.getIdsCaracteristicas());
        }

        mascota.setNombreMascota(request.getNombreMascota());
        mascota.setDescripcion(request.getDescripcion());
        mascota.setColorPrimario(request.getColorPrimario());
        mascota.setColorSecundario(request.getColorSecundario());
        mascota.setRaza(raza);
        mascota.setTamanio(tamanio);
        mascota.setCaracteristicas(caracteristicas);
        mascota.setEdadAproximada(request.getEdadAproximada());

        if (request.getUrlsFotografias() != null && !request.getUrlsFotografias().isEmpty()) {
            mascota.getFotografias().clear();
            List<Fotografia> fotografias = request.getUrlsFotografias().stream()
                    .map(url -> Fotografia.builder()
                            .urlFotografia(url)
                            .mascota(mascota)
                            .build())
                    .collect(Collectors.toList());
            mascota.getFotografias().addAll(fotografias);
        } else if (mascota.getFotografias() != null) {
            mascota.getFotografias().clear();
        }

        Mascota mascotaActualizada = mascotaRepository.save(mascota);
        return mapToResponseDTO(mascotaActualizada);
    }

    @Override
    @Transactional
    public void deleteMascota(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con id: " + id));
        mascotaRepository.delete(mascota);
    }

    private MascotaResponseDTO mapToResponseDTO(Mascota mascota) {
        return MascotaResponseDTO.builder()
                .idMascota(mascota.getIdMascota())
                .nombreMascota(mascota.getNombreMascota())
                .descripcion(mascota.getDescripcion())
                .colorPrimario(mascota.getColorPrimario())
                .colorSecundario(mascota.getColorSecundario())
                .nombreRaza(mascota.getRaza() != null ? mascota.getRaza().getNombreRaza() : null)
                .especieRaza((mascota.getRaza() != null && mascota.getRaza().getEspecie() != null) ? mascota.getRaza().getEspecie().getNombreEspecie() : null)
                .descripcionTamanio(mascota.getTamanio() != null ? mascota.getTamanio().getDescripcionTamanio() : null)
                .nombreDueno(mascota.getUsuario() != null ? mascota.getUsuario().getNombre() : "Sin Dueño")
                .caracteristicas(mascota.getCaracteristicas() != null ?
                        mascota.getCaracteristicas().stream().map(Caracteristica::getDescripcion).collect(Collectors.toList()) : new ArrayList<>())
                .urlsFotografias(mascota.getFotografias() != null ?
                        mascota.getFotografias().stream().map(Fotografia::getUrlFotografia).collect(Collectors.toList()) : new ArrayList<>())
                .edadAproximada(mascota.getEdadAproximada())
                .build();
    }
}
