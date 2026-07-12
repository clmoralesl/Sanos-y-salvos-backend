package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MascotaRepositoryTest {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Test
    public void testSaveMascota() {
        Mascota mascota = Mascota.builder()
                .nombreMascota("Firulais")
                .colorPrimario("Café")
                .descripcion("Perro amigable")
                .build();

        Mascota saved = mascotaRepository.save(mascota);

        assertNotNull(saved.getIdMascota());
        assertEquals("Firulais", saved.getNombreMascota());
    }

    @Test
    public void testSaveWithFotografiasCascade() {
        Mascota mascota = Mascota.builder()
                .nombreMascota("Firulais")
                .build();

        Fotografia foto = Fotografia.builder()
                .urlFotografia("http://image.com/foto.jpg")
                .mascota(mascota)
                .build();

        mascota.setFotografias(new ArrayList<>(Collections.singletonList(foto)));

        Mascota saved = mascotaRepository.save(mascota);

        assertNotNull(saved.getIdMascota());
        assertEquals(1, saved.getFotografias().size());
        assertEquals("http://image.com/foto.jpg", saved.getFotografias().get(0).getUrlFotografia());
    }
}
