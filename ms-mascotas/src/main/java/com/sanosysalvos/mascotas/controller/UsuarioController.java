package com.sanosysalvos.mascotas.controller;

import com.sanosysalvos.mascotas.dto.UsuarioRequestDTO;
import com.sanosysalvos.mascotas.dto.UsuarioResponseDTO;
import com.sanosysalvos.mascotas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Endpoint para registrar un nuevo usuario luego de que inicie sesión en Auth0.
     * En el futuro, el API Gateway llamará o permitirá este paso.
     */
    @PostMapping("/registro")
    public ResponseEntity<Map<String, Object>> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO data = usuarioService.registrarUsuario(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario registrado correctamente");
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint protegido. Devuelve el perfil del usuario utilizando la cabecera X-Auth0-Id.
     * Supone que el API Gateway inyecta esta cabecera tras validar el JWT de Auth0. 
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obtenerMiPerfil(@RequestHeader("X-Auth0-Id") String auth0Id) {
        UsuarioResponseDTO miPerfil = usuarioService.obtenerPerfilPorAuth0Id(auth0Id);
        return ResponseEntity.ok(miPerfil);
    }

    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> actualizarMiPerfil(@RequestHeader("X-Auth0-Id") String auth0Id, @Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO data = usuarioService.actualizarUsuario(auth0Id, request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Su perfil ha sido actualizado correctamente");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Map<String, Object>> eliminarMiPerfil(@RequestHeader("X-Auth0-Id") String auth0Id) {
        usuarioService.eliminarUsuario(auth0Id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Su cuenta ha sido eliminada correctamente");
        return ResponseEntity.ok(response);
    }

    // --- Endpoints Administrativos ---

    @GetMapping
    public ResponseEntity<java.util.List<UsuarioResponseDTO>> obtenerTodosLosUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarUsuarioPorId(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO data = usuarioService.actualizarUsuarioPorId(id, request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario actualizado correctamente");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarUsuarioPorId(@PathVariable Long id) {
        usuarioService.eliminarUsuarioPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario " + id + " eliminado correctamente");
        return ResponseEntity.ok(response);
    }
}
