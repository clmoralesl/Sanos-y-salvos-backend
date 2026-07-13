package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    
    Optional<Usuario> findByAuth0Id(String auth0Id);

    java.util.List<Usuario> findByOrganizacionIdOrganizacion(Long idOrganizacion);

    
    Optional<Usuario> findByEmail(String email);

    boolean existsByAuth0Id(String auth0Id);
}

