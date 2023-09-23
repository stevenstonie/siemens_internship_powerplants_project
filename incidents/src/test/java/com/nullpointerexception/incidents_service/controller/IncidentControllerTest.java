package com.nullpointerexception.incidents_service.controller;


import com.nullpointerexception.incidents_service.model.Incident;
import com.nullpointerexception.incidents_service.model.IncidentDTO;
import com.nullpointerexception.incidents_service.model.PowerPlant;
import com.nullpointerexception.incidents_service.model.User;
import com.nullpointerexception.incidents_service.repository.PowerPlantRepository;
import com.nullpointerexception.incidents_service.repository.UserRepository;
import com.nullpointerexception.incidents_service.service.IncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class IncidentControllerTest {


    @Mock
    private PowerPlantRepository powerPlantRepository;

    @Mock
    private IncidentService incidentService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IncidentController incidentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateIncident() {
        // Creare mock-uri pentru obiectele necesare
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setDescription("Test Incident");
        incidentDTO.setSeverity("High");
        incidentDTO.setStatus("Open");
        incidentDTO.setReportedBy(new User());
        incidentDTO.getReportedBy().setEmail("test@test.com");
        incidentDTO.setAssignedTo(new User());
        incidentDTO.getAssignedTo().setEmail("test2@test.com");

        PowerPlant powerPlant = new PowerPlant();
        powerPlant.setId(1L);

        Incident incident = new Incident();
        incident.setDescription("Test Incident");
        incident.setSeverity("High");
        incident.setStatus("Open");
        incident.setReportedBy(new User());
        incident.setAssignedTo(new User());
        incident.setTimestamp(OffsetDateTime.now());

        // Setare comportament pentru mock-uri
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(new User()));
        when(powerPlantRepository.findById(any(Long.class))).thenReturn(Optional.of(powerPlant));
        when(incidentService.createIncident(any(IncidentDTO.class))).thenReturn(incident);

        // Apel metoda care se testeaza
        ResponseEntity<?> responseEntity = incidentController.createIncident(1L, incidentDTO);

        // Verificare raspuns
        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals(incident, responseEntity.getBody());
    }
}
