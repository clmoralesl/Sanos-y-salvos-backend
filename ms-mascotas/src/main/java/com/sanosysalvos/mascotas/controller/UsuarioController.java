package com.sanosysalvos.mascotas.controller;

import com.sanosysalvos.mascotas.dto.UsuarioRequestDTO;
import com.sanosysalvos.mascotas.dto.UsuarioResponseDTO;
import com.sanosysalvos.mascotas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mascotas/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO data = usuarioService.registrarUsuario(request);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obtenerMiPerfil(@RequestHeader("X-Auth0-Id") String auth0Id) {
        UsuarioResponseDTO miPerfil = usuarioService.obtenerPerfilPorAuth0Id(auth0Id);
        return ResponseEntity.ok(miPerfil);
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> actualizarMiPerfil(@RequestHeader("X-Auth0-Id") String auth0Id, @Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO data = usuarioService.actualizarUsuario(auth0Id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> eliminarMiPerfil(@RequestHeader("X-Auth0-Id") String auth0Id) {
        usuarioService.eliminarUsuario(auth0Id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<java.util.List<UsuarioResponseDTO>> obtenerTodosLosUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuarioPorId(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO data = usuarioService.actualizarUsuarioPorId(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuarioPorId(@PathVariable Long id) {
        usuarioService.eliminarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/membresia")
    public ResponseEntity<UsuarioResponseDTO> actualizarMembresia(@PathVariable Long id, @RequestParam String estado) {
        UsuarioResponseDTO data = usuarioService.actualizarMembresia(id, estado);
        return ResponseEntity.ok(data);
    }
}
