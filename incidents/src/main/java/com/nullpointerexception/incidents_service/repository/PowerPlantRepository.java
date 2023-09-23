package com.nullpointerexception.incidents_service.repository;

import com.nullpointerexception.incidents_service.model.PowerPlant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerPlantRepository extends JpaRepository<PowerPlant, Long> {
}
