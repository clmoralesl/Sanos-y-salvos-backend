package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Component
public class CaracteristicasStrategy implements SimilitudStrategy {

    private static final double MAX_PUNTAJE = 10.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {

        List<String> baseAtributos = base.getCaracteristicas();
        List<String> candidatoAtributos = candidato.getCaracteristicas();

        if (baseAtributos == null || baseAtributos.isEmpty()
                || candidatoAtributos == null || candidatoAtributos.isEmpty()) {
            return 0.0;
        }

        double puntaje = 0.0;

        for (String caracteristicaBase : baseAtributos) {

            String baseNormalizada = normalizar(caracteristicaBase);

            boolean coincide = candidatoAtributos.stream()
                    .map(this::normalizar)
                    .anyMatch(baseNormalizada::equals);

            if (coincide) {
                puntaje += obtenerPuntaje(baseNormalizada);
            }
        }

        return Math.min(MAX_PUNTAJE, puntaje);
    }

    private double obtenerPuntaje(String caracteristica) {

        return switch (caracteristica) {

            // Rasgos físicos muy identificadores
            case "ojos de distinto color",
                 "ceguera visible",
                 "cicatriz visible",
                 "sin cola",
                 "mancha en el ojo" -> 3.0;

            // Rasgos físicos útiles
            case "cola corta",
                 "patas blancas",
                 "mancha blanca en el pecho",
                 "orejas caidas",
                 "orejas erguidas" -> 1.5;

            // Elementos circunstanciales
            case "usa collar",
                 "usa arnes",
                 "requiere medicacion" -> 0.5;

            // Comportamiento: aporte mínimo
            case "jugueton",
                 "timido / asustadizo",
                 "amigable con ninos",
                 "agresivo con otros animales",
                 "carinoso",
                 "muy activo" -> 0.2;

            default -> 0.0;
        };
    }

    private String normalizar(String valor) {

        if (valor == null) {
            return "";
        }

        String sinTildes = Normalizer
                .normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return sinTildes
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}