package com.sanosysalvos.msgeo.repository;

import com.sanosysalvos.msgeo.model.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {
    List<Comuna> findByRegionId(Long regionId);
}
