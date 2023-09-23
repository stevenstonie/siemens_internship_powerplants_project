package com.nullpointerexception.incidents_service.service;

import com.nullpointerexception.incidents_service.model.Incident;
import com.nullpointerexception.incidents_service.model.IncidentDTO;
import com.nullpointerexception.incidents_service.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

 import java.time.OffsetDateTime;

@Service
public class IncidentService {
    @Autowired
    private IncidentRepository incidentRepository;

    public Incident createIncident(IncidentDTO incidentDTO) {

        Incident incident = new Incident();

        // Setează câmpurile necesare din DTO în entitatea Incident
        incident.setPowerPlant(incidentDTO.getPowerPlant());
        incident.setDescription(incidentDTO.getDescription());
        incident.setSeverity(incidentDTO.getSeverity());
        incident.setStatus(incidentDTO.getStatus());
        incident.setReportedBy(incidentDTO.getReportedBy());
        incident.setAssignedTo(incidentDTO.getAssignedTo());
        incident.setTimestamp(OffsetDateTime.now());
        incident.setResolution(incidentDTO.getResolution());

        if (incidentDTO.getStatus().equals("Closed")) {

            incident.setResolvedAt(OffsetDateTime.now());
        }

        Incident savedIncident = incidentRepository.save(incident);
        System.out.println("Incident salvat: " + savedIncident);

         return savedIncident;
    }


}