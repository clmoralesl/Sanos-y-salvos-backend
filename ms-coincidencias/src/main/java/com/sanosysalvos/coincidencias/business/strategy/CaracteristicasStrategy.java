package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.CaracteristicaDTO;
import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaracteristicasStrategy implements SimilitudStrategy {

    
    private static final double MAX_PUNTAJE = 50.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        List<CaracteristicaDTO> baseAtributos = base.getCaracteristicasFisicas();
        List<CaracteristicaDTO> candAtributos = candidato.getCaracteristicasFisicas();

        if (baseAtributos == null || baseAtributos.isEmpty() || candAtributos == null || candAtributos.isEmpty()) {
            return 0.0;
        }

        int coincidencias = 0;
        for (CaracteristicaDTO b : baseAtributos) {
            for (CaracteristicaDTO c : candAtributos) {
                if (b.getId().equals(c.getId())) {
                    coincidencias++;
                    break;
                }
            }
        }

        
        double proporcion = (double) coincidencias / baseAtributos.size();
        return proporcion * MAX_PUNTAJE;
    }
}

