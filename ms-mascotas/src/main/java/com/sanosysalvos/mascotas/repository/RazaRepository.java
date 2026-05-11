package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.Raza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RazaRepository extends JpaRepository<Raza, Long> {
}
