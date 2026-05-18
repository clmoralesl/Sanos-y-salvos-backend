package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.entity.TipoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCuentaRepository extends JpaRepository<TipoCuenta, Long> {
}

