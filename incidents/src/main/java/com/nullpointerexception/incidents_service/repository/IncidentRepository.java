package com.nullpointerexception.incidents_service.repository;

import com.nullpointerexception.incidents_service.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
}
