package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testSaveUsuarioSuccess() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan Perez")
                .email("juan@gmail.com")
                .auth0Id("auth0|123456")
                .build();

        Usuario saved = usuarioRepository.save(usuario);

        assertNotNull(saved.getIdUsuario());
        assertEquals("juan@gmail.com", saved.getEmail());
    }

    @Test
    public void testUniqueEmailConstraint() {
        Usuario u1 = Usuario.builder()
                .nombre("Juan")
                .email("juan@gmail.com")
                .auth0Id("auth1")
                .build();
        usuarioRepository.save(u1);

        Usuario u2 = Usuario.builder()
                .nombre("Pedro")
                .email("juan@gmail.com")
                .auth0Id("auth2")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            usuarioRepository.saveAndFlush(u2);
        });
    }
}
