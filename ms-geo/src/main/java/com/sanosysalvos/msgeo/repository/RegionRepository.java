package com.sanosysalvos.msgeo.repository;

import com.sanosysalvos.msgeo.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}

