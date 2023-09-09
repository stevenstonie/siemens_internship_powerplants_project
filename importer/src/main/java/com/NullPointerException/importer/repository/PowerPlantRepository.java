package com.NullPointerException.importer.repository;

import com.NullPointerException.importer.model.PowerPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerPlantRepository extends JpaRepository<PowerPlant, String> {
}
