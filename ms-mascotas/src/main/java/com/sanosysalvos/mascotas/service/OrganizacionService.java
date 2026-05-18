package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.dto.OrganizacionRequestDTO;
import com.sanosysalvos.mascotas.dto.OrganizacionResponseDTO;

import java.util.List;

public interface OrganizacionService {
    OrganizacionResponseDTO crearOrganizacion(OrganizacionRequestDTO request);
    OrganizacionResponseDTO obtenerOrganizacionPorId(Long id);
    List<OrganizacionResponseDTO> obtenerTodas();
    OrganizacionResponseDTO actualizarOrganizacion(Long id, OrganizacionRequestDTO request);
    void eliminarOrganizacion(Long id);
}

