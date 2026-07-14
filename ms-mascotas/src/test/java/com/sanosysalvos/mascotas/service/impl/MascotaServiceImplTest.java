package com.sanosysalvos.mascotas.service.impl;

import com.sanosysalvos.mascotas.dto.MascotaRequestDTO;
import com.sanosysalvos.mascotas.dto.MascotaResponseDTO;
import com.sanosysalvos.mascotas.entity.*;
import com.sanosysalvos.mascotas.exception.ResourceNotFoundException;
import com.sanosysalvos.mascotas.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceImplTest {

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
    private MascotaServiceImpl mascotaService;

    @Test
    void createMascota_WithValidData_ReturnsResponseDTO() {
        MascotaRequestDTO request = new MascotaRequestDTO();
        request.setNombreMascota("Fido");
        request.setIdRaza(1L);
        request.setIdTamanio(1L);
        request.setSinDueno(false);

        Usuario usuario = new Usuario();
        usuario.setAuth0Id("auth0|123");

        Raza raza = new Raza();
        raza.setIdRaza(1L);
        Especie especie = new Especie();
        especie.setNombreEspecie("Perro");
        raza.setEspecie(especie);

        Tamanio tamanio = new Tamanio();
        tamanio.setIdTamanio(1L);

        Mascota mascota = new Mascota();
        mascota.setIdMascota(1L);
        mascota.setNombreMascota("Fido");
        mascota.setUsuario(usuario);
        mascota.setRaza(raza);
        mascota.setTamanio(tamanio);

        when(usuarioRepository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(usuario));
        when(razaRepository.findById(1L)).thenReturn(Optional.of(raza));
        when(tamanioRepository.findById(1L)).thenReturn(Optional.of(tamanio));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota);

        MascotaResponseDTO result = mascotaService.createMascota(request, "auth0|123");

        assertNotNull(result);
        assertEquals("Fido", result.getNombreMascota());
        verify(mascotaRepository).save(any(Mascota.class));
    }

    @Test
    void createMascota_UserNotFound_ThrowsResourceNotFoundException() {
        MascotaRequestDTO request = new MascotaRequestDTO();
        request.setSinDueno(false);

        when(usuarioRepository.findByAuth0Id("auth0|123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mascotaService.createMascota(request, "auth0|123"));
    }

    @Test
    void getMascotaById_Exists_ReturnsResponseDTO() {
        Mascota mascota = new Mascota();
        mascota.setIdMascota(1L);
        mascota.setNombreMascota("Fido");

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        MascotaResponseDTO result = mascotaService.getMascotaById(1L);

        assertNotNull(result);
        assertEquals("Fido", result.getNombreMascota());
    }

    @Test
    void getMascotaById_NotExists_ThrowsResourceNotFoundException() {
        when(mascotaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mascotaService.getMascotaById(1L));
    }

    @Test
    void getAllMascotas_ReturnsList() {
        Mascota mascota = new Mascota();
        mascota.setIdMascota(1L);
        mascota.setNombreMascota("Fido");

        when(mascotaRepository.findAll()).thenReturn(List.of(mascota));

        List<MascotaResponseDTO> result = mascotaService.getAllMascotas();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getMisMascotas_ReturnsList() {
        Mascota mascota = new Mascota();
        mascota.setIdMascota(1L);
        mascota.setNombreMascota("Fido");

        when(mascotaRepository.findByUsuarioAuth0Id("auth0|123")).thenReturn(List.of(mascota));

        List<MascotaResponseDTO> result = mascotaService.getMisMascotas("auth0|123");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void updateMascota_ExistsAndSameUser_ReturnsResponseDTO() {
        MascotaRequestDTO request = new MascotaRequestDTO();
        request.setNombreMascota("Fido Nuevo");
        request.setIdRaza(1L);
        request.setIdTamanio(1L);

        Usuario usuario = new Usuario();
        usuario.setAuth0Id("auth0|123");

        Raza raza = new Raza();
        raza.setIdRaza(1L);

        Tamanio tamanio = new Tamanio();
        tamanio.setIdTamanio(1L);

        Mascota mascota = new Mascota();
        mascota.setIdMascota(1L);
        mascota.setUsuario(usuario);
        mascota.setFotografias(Collections.emptyList());

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(razaRepository.findById(1L)).thenReturn(Optional.of(raza));
        when(tamanioRepository.findById(1L)).thenReturn(Optional.of(tamanio));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota);

        MascotaResponseDTO result = mascotaService.updateMascota(1L, request, "auth0|123");

        assertNotNull(result);
        verify(mascotaRepository).save(any(Mascota.class));
    }

    @Test
    void updateMascota_DifferentUser_ThrowsRuntimeException() {
        MascotaRequestDTO request = new MascotaRequestDTO();

        Usuario usuario = new Usuario();
        usuario.setAuth0Id("auth0|abc");

        Mascota mascota = new Mascota();
        mascota.setIdMascota(1L);
        mascota.setUsuario(usuario);

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        assertThrows(RuntimeException.class, () -> mascotaService.updateMascota(1L, request, "auth0|123"));
    }

    @Test
    void deleteMascota_Exists_DeletesMascota() {
        Mascota mascota = new Mascota();
        mascota.setIdMascota(1L);

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        mascotaService.deleteMascota(1L);

        verify(mascotaRepository).delete(mascota);
    }
}
