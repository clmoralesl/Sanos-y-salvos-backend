package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;

public interface SimilitudStrategy {

    double calcularPuntaje(MascotaDTO base, MascotaDTO candidato);
}

