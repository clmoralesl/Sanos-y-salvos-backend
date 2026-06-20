package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.dto.CatalogoResponseDTO;
import java.util.List;

public interface CatalogoService {
    List<CatalogoResponseDTO> obtenerRazas();
    List<CatalogoResponseDTO> obtenerTamanios();
    List<CatalogoResponseDTO> obtenerCaracteristicas();
    List<CatalogoResponseDTO> obtenerTiposReporte();
    List<CatalogoResponseDTO> obtenerTiposCuenta();
    List<CatalogoResponseDTO> obtenerEspecies();
}
