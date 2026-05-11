package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.MascotaRequestDTO;
import com.sanosysalvos.mascotas.dto.MascotaResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.exception.ResourceNotFoundException;
import com.sanosysalvos.mascotas.repository.CaracteristicaRepository;
import com.sanosysalvos.mascotas.repository.MascotaRepository;
import com.sanosysalvos.mascotas.repository.RazaRepository;
import com.sanosysalvos.mascotas.repository.TamanioRepository;
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

    @Override
    @Transactional
    public MascotaResponseDTO createMascota(MascotaRequestDTO request) {
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
                .raza(raza)
                .tamanio(tamanio)
                .caracteristicas(caracteristicas)
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
    @Transactional
    public MascotaResponseDTO updateMascota(Long id, MascotaRequestDTO request) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con id: " + id));

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
        mascota.setRaza(raza);
        mascota.setTamanio(tamanio);
        mascota.setCaracteristicas(caracteristicas);

        if (request.getUrlsFotografias() != null && !request.getUrlsFotografias().isEmpty()) {
            // Simple replace of photos. Typically you would diff them, but we clear and add.
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
                .nombreRaza(mascota.getRaza() != null ? mascota.getRaza().getNombreRaza() : null)
                .especieRaza((mascota.getRaza() != null && mascota.getRaza().getEspecie() != null) ? mascota.getRaza().getEspecie().getNombreEspecie() : null)
                .descripcionTamanio(mascota.getTamanio() != null ? mascota.getTamanio().getDescripcionTamanio() : null)
                .caracteristicas(mascota.getCaracteristicas() != null ?
                        mascota.getCaracteristicas().stream().map(Caracteristica::getDescripcion).collect(Collectors.toList()) : new ArrayList<>())
                .urlsFotografias(mascota.getFotografias() != null ?
                        mascota.getFotografias().stream().map(Fotografia::getUrlFotografia).collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
