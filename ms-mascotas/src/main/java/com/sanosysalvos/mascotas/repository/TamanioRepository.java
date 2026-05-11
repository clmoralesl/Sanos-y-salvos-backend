package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.Tamanio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TamanioRepository extends JpaRepository<Tamanio, Long> {
}
