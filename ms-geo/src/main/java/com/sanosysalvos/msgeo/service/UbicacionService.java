package com.sanosysalvos.msgeo.service;

import com.sanosysalvos.msgeo.dto.UbicacionRequestDTO;
import com.sanosysalvos.msgeo.model.UbicacionReporte;
import java.util.List;

public interface UbicacionService {
    Long crearUbicacion(UbicacionRequestDTO request);
    boolean existeUbicacion(Long id);
    UbicacionReporte obtenerUbicacionPorId(Long id);
    List<Long> obtenerUbicacionesEnRadio(Long idUbicacion, int k);
}

