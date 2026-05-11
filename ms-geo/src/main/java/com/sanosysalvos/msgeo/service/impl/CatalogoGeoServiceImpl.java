package com.sanosysalvos.msgeo.service.impl;

import com.sanosysalvos.msgeo.model.Comuna;
import com.sanosysalvos.msgeo.model.Region;
import com.sanosysalvos.msgeo.repository.ComunaRepository;
import com.sanosysalvos.msgeo.repository.RegionRepository;
import com.sanosysalvos.msgeo.service.CatalogoGeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoGeoServiceImpl implements CatalogoGeoService {

    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;

    @Override
    public List<Region> obtenerRegiones() {
        return regionRepository.findAll();
    }

    @Override
    public List<Comuna> obtenerComunasPorRegion(Long idRegion) {
        return comunaRepository.findByRegionId(idRegion);
    }
}
