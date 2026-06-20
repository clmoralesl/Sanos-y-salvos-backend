package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaracteristicasStrategy implements SimilitudStrategy {

    private static final double MAX_PUNTAJE = 20.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        List<String> baseAtributos = base.getCaracteristicas();
        List<String> candAtributos = candidato.getCaracteristicas();

        if (baseAtributos == null || baseAtributos.isEmpty() || candAtributos == null || candAtributos.isEmpty()) {
            return 0.0;
        }

        int coincidencias = 0;
        for (String b : baseAtributos) {
            for (String c : candAtributos) {
                if (b.equalsIgnoreCase(c)) {
                    coincidencias++;
                    break;
                }
            }
        }

        double proporcion = (double) coincidencias / baseAtributos.size();
        return proporcion * MAX_PUNTAJE;
    }
}

