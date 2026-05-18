package com.sanosysalvos.msgeo.repository;

import com.sanosysalvos.msgeo.model.ZonaGeo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaGeoRepository extends JpaRepository<ZonaGeo, String> {
}

