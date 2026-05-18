package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByUsuarioAuth0Id(String auth0Id);
}

