package com.sanosysalvos.coincidencias.business.strategy;

import com.sanosysalvos.coincidencias.integration.dto.MascotaDTO;
import org.springframework.stereotype.Component;

@Component
public class ColorStrategy implements SimilitudStrategy {

    private static final double MAX_PUNTAJE = 35.0;

    @Override
    public double calcularPuntaje(MascotaDTO base, MascotaDTO candidato) {
        String p1 = normalize(base.getColorPrimario());
        String p2 = normalize(candidato.getColorPrimario());
        String s1 = normalize(base.getColorSecundario());
        String s2 = normalize(candidato.getColorSecundario());

        if (p1.isEmpty() && p2.isEmpty()) {
            return 0.0;
        }

        double score = 0.0;

        if (p1.equals(p2) && !p1.isEmpty()) {
            score += 10.0;
            if (s1.equals(s2) && !s1.isEmpty() && !s1.contains("ningun")) {
                score += 5.0;
            }
        } else if (areColorsSimilar(p1, p2)) {
            score += 5.0;
            if (s1.equals(s2) && !s1.isEmpty() && !s1.contains("ningun")) {
                score += 2.0;
            }
        } else {
            boolean p1MatchesS2 = !p1.isEmpty() && p1.equals(s2);
            boolean p2MatchesS1 = !p2.isEmpty() && p2.equals(s1);

            if (p1MatchesS2 && p2MatchesS1) {
                score += 8.0;
            } else if (p1MatchesS2 || p2MatchesS1) {
                score += 4.0;
            } else if (areColorsSimilar(p1, s2) || areColorsSimilar(p2, s1)) {
                score += 2.0;
            }
        }

        return score;
    }

    private boolean areColorsSimilar(String c1, String c2) {
        if (c1.isEmpty() || c2.isEmpty()) {
            return false;
        }
        if (c1.equals(c2)) {
            return true;
        }
        if ((c1.equals("negro") && c2.equals("gris")) || (c1.equals("gris") && c2.equals("negro"))) {
            return true;
        }
        if ((c1.equals("cafe") && (c2.equals("amarillo") || c2.equals("naranja"))) 
                || ((c2.equals("cafe") && (c1.equals("amarillo") || c1.equals("naranja"))))) {
            return true;
        }
        if ((c1.equals("amarillo") && (c2.equals("crema") || c2.equals("naranja"))) 
                || ((c2.equals("amarillo") && (c1.equals("crema") || c1.equals("naranja"))))) {
            return true;
        }
        if ((c1.equals("blanco") && c2.equals("crema")) || (c1.equals("crema") && c2.equals("blanco"))) {
            return true;
        }
        return false;
    }

    private String normalize(String val) {
        if (val == null) {
            return "";
        }
        String clean = val.trim().toLowerCase();
        if (clean.contains("cafe") || clean.contains("café") || clean.contains("marr")) {
            return "cafe";
        }
        if (clean.contains("amarill") || clean.contains("rubio")) {
            return "amarillo";
        }
        if (clean.contains("naranj")) {
            return "naranja";
        }
        return clean;
    }
}
