package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.CatalogoResponseDTO;
import com.sanosysalvos.mascotas.repository.*;
import com.sanosysalvos.mascotas.service.CatalogoService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    private final RazaRepository razaRepository;
    private final TamanioRepository tamanioRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final TipoReporteRepository tipoReporteRepository;
    private final TipoCuentaRepository tipoCuentaRepository;
    private final EspecieRepository especieRepository;

    public CatalogoServiceImpl(RazaRepository razaRepository, TamanioRepository tamanioRepository,
                               CaracteristicaRepository caracteristicaRepository, TipoReporteRepository tipoReporteRepository,
                               TipoCuentaRepository tipoCuentaRepository, EspecieRepository especieRepository) {
        this.razaRepository = razaRepository;
        this.tamanioRepository = tamanioRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.tipoReporteRepository = tipoReporteRepository;
        this.tipoCuentaRepository = tipoCuentaRepository;
        this.especieRepository = especieRepository;
    }

    @Override
    public List<CatalogoResponseDTO> obtenerRazas() {
        return razaRepository.findAll().stream()
                .map(r -> CatalogoResponseDTO.builder()
                        .id(r.getIdRaza())
                        .descripcion(r.getNombreRaza())
                        .idEspecie(r.getEspecie() != null ? r.getEspecie().getIdEspecie() : null)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CatalogoResponseDTO> obtenerTamanios() {
        return tamanioRepository.findAll().stream()
                .map(t -> CatalogoResponseDTO.builder().id(t.getIdTamanio()).descripcion(t.getDescripcionTamanio()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CatalogoResponseDTO> obtenerCaracteristicas() {
        return caracteristicaRepository.findAll().stream()
                .map(c -> CatalogoResponseDTO.builder().id(c.getIdCaracteristica()).descripcion(c.getDescripcion()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CatalogoResponseDTO> obtenerTiposReporte() {
        return tipoReporteRepository.findAll().stream()
                .map(tr -> CatalogoResponseDTO.builder().id(tr.getIdTipoReporte()).descripcion(tr.getDescripcion()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CatalogoResponseDTO> obtenerTiposCuenta() {
        return tipoCuentaRepository.findAll().stream()
                .map(tc -> CatalogoResponseDTO.builder().id(tc.getIdTipoCuenta()).descripcion(tc.getDescripcion()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CatalogoResponseDTO> obtenerEspecies() {
        return especieRepository.findAll().stream()
                .map(e -> CatalogoResponseDTO.builder()
                        .id(e.getIdEspecie())
                        .descripcion(e.getNombreEspecie())
                        .build())
                .collect(Collectors.toList());
    }
}
