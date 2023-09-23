package com.nullpointerexception.incidents_service.service;

import com.nullpointerexception.incidents_service.model.Incident;
import com.nullpointerexception.incidents_service.model.IncidentDTO;
import com.nullpointerexception.incidents_service.repository.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceTest {

    @InjectMocks
    private IncidentService incidentService;

    @Mock
    private IncidentRepository incidentRepository;

    @Test
    public void testCreateIncident() {
        //IncidentDTO de test
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setDescription("Test incident");
        incidentDTO.setSeverity("High");
        incidentDTO.setStatus("Open");

        //  metoda save()pt ca să returneze incidentul
        when(incidentRepository.save(any(Incident.class))).thenAnswer(i -> i.getArguments()[0]);

        Incident result = incidentService.createIncident(incidentDTO);
        assertEquals("Test incident", result.getDescription());
        assertEquals("High", result.getSeverity());
        assertEquals("Open", result.getStatus());
        assertNotNull(result.getTimestamp());

        // Verificăm că metoda save() a fost apelată
        verify(incidentRepository).save(any(Incident.class));



    }
    @Test
    public void testCreateIncidentStatusClosed() {
        // IncidentDTO de test
        IncidentDTO incidentDTO = new IncidentDTO();
        incidentDTO.setDescription("Test incident");
        incidentDTO.setSeverity("High");
        incidentDTO.setStatus("Closed"); //  "Closed" pentru a verifica metoda setResolvedAt()

        //  save() a repository-ului pt ca să returneze incidentul
        when(incidentRepository.save(any(Incident.class))).thenAnswer(i -> i.getArguments()[0]);

        // OffsetDateTime.now()  returneza un timp fix
        OffsetDateTime fixedTime = OffsetDateTime.now();
        mockStatic(OffsetDateTime.class);
        when(OffsetDateTime.now()).thenReturn(fixedTime);

        Incident result = incidentService.createIncident(incidentDTO);

        verify(incidentRepository).save(any(Incident.class));

        // Verificăm că timpul de rezolvare a fost setat corect
        assertEquals(fixedTime, result.getResolvedAt());
    }

}
