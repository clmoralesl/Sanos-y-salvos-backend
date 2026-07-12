package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.MascotaRequestDTO;
import com.sanosysalvos.mascotas.dto.MascotaResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.exception.ResourceNotFoundException;
import com.sanosysalvos.mascotas.repository.CaracteristicaRepository;
import com.sanosysalvos.mascotas.repository.MascotaRepository;
import com.sanosysalvos.mascotas.repository.RazaRepository;
import com.sanosysalvos.mascotas.repository.TamanioRepository;
import com.sanosysalvos.mascotas.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MascotaServiceImplTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private RazaRepository razaRepository;

    @Mock
    private TamanioRepository tamanioRepository;

    @Mock
    private CaracteristicaRepository caracteristicaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MascotaServiceImpl service;

    @Test
    public void testGetMascotaByIdNotFound() {
        when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getMascotaById(999L));
    }

    @Test
    public void testGetMascotaByIdSuccess() {
        Mascota mascota = Mascota.builder()
                .idMascota(1L)
                .nombreMascota("Firulais")
                .raza(Raza.builder().nombreRaza("Pastor Aleman").build())
                .tamanio(Tamanio.builder().descripcionTamanio("Grande").build())
                .build();
        
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        MascotaResponseDTO response = service.getMascotaById(1L);

        assertNotNull(response);
        assertEquals("Firulais", response.getNombreMascota());
        assertEquals("Pastor Aleman", response.getNombreRaza());
    }

    @Test
    public void testCreateMascotaSuccess() {
        MascotaRequestDTO request = MascotaRequestDTO.builder()
                .nombreMascota("Firulais")
                .idRaza(1L)
                .idTamanio(2L)
                .sinDueno(true)
                .build();

        Raza raza = Raza.builder().idRaza(1L).nombreRaza("Pastor Aleman").build();
        Tamanio tamanio = Tamanio.builder().idTamanio(2L).descripcionTamanio("Grande").build();

        when(razaRepository.findById(1L)).thenReturn(Optional.of(raza));
        when(tamanioRepository.findById(2L)).thenReturn(Optional.of(tamanio));
        
        Mascota saved = Mascota.builder()
                .idMascota(100L)
                .nombreMascota("Firulais")
                .raza(raza)
                .tamanio(tamanio)
                .build();
        
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(saved);

        MascotaResponseDTO response = service.createMascota(request, "auth0-id");

        assertNotNull(response);
        assertEquals(100L, response.getIdMascota());
        assertEquals("Firulais", response.getNombreMascota());
    }
}
