package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.dto.UsuarioRequestDTO;
import com.sanosysalvos.mascotas.dto.UsuarioResponseDTO;

public interface UsuarioService {
    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO request);
    UsuarioResponseDTO obtenerPerfilPorAuth0Id(String auth0Id);
    UsuarioResponseDTO actualizarUsuario(String auth0Id, UsuarioRequestDTO request);
    void eliminarUsuario(String auth0Id);

    // Métodos administrativos
    java.util.List<UsuarioResponseDTO> obtenerTodosLosUsuarios();
    UsuarioResponseDTO obtenerUsuarioPorId(Long id);
    UsuarioResponseDTO actualizarUsuarioPorId(Long id, UsuarioRequestDTO request);
    void eliminarUsuarioPorId(Long id);
}
