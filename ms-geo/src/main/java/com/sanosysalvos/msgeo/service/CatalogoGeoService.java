package com.sanosysalvos.msgeo.service;

import com.sanosysalvos.msgeo.model.Comuna;
import com.sanosysalvos.msgeo.model.Region;

import java.util.List;

public interface CatalogoGeoService {
    List<Region> obtenerRegiones();
    List<Comuna> obtenerComunasPorRegion(Long idRegion);
}
