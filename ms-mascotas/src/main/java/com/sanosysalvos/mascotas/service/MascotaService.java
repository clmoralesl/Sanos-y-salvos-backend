package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.dto.MascotaRequestDTO;
import com.sanosysalvos.mascotas.dto.MascotaResponseDTO;

import java.util.List;

public interface MascotaService {
    MascotaResponseDTO createMascota(MascotaRequestDTO request);
    MascotaResponseDTO getMascotaById(Long id);
    List<MascotaResponseDTO> getAllMascotas();
    MascotaResponseDTO updateMascota(Long id, MascotaRequestDTO request);
    void deleteMascota(Long id);
}
