package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.dto.MascotaRequestDTO;
import com.sanosysalvos.mascotas.dto.MascotaResponseDTO;

import java.util.List;

public interface MascotaService {
    MascotaResponseDTO createMascota(MascotaRequestDTO request, String auth0Id);
    MascotaResponseDTO getMascotaById(Long id);
    List<MascotaResponseDTO> getAllMascotas();
    List<MascotaResponseDTO> getMisMascotas(String auth0Id);
    MascotaResponseDTO updateMascota(Long id, MascotaRequestDTO request, String auth0Id);
    void deleteMascota(Long id);
}

