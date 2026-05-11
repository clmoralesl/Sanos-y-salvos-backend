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

    @Override
    public OrganizacionResponseDTO crearOrganizacion(OrganizacionRequestDTO request) {
        Organizacion organizacion = Organizacion.builder()
                .nombreOrganizacion(request.getNombreOrganizacion())
                .direccion(request.getDireccion())
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
        
        Organizacion actualizada = organizacionRepository.save(organizacion);
        return mapToDTO(actualizada);
    }

    @Override
    public void eliminarOrganizacion(Long id) {
        Organizacion organizacion = organizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada con ID: " + id));
        organizacionRepository.delete(organizacion);
    }

    private OrganizacionResponseDTO mapToDTO(Organizacion organizacion) {
        return OrganizacionResponseDTO.builder()
                .idOrganizacion(organizacion.getIdOrganizacion())
                .nombreOrganizacion(organizacion.getNombreOrganizacion())
                .direccion(organizacion.getDireccion())
                .build();
    }
}
