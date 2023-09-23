package com.nullpointerexception.incidents_service.controller;

import com.nullpointerexception.incidents_service.model.Incident;
import com.nullpointerexception.incidents_service.model.IncidentDTO;
import com.nullpointerexception.incidents_service.model.PowerPlant;
import com.nullpointerexception.incidents_service.model.User;
import com.nullpointerexception.incidents_service.producer.IncidentProducer;
import com.nullpointerexception.incidents_service.repository.PowerPlantRepository;
import com.nullpointerexception.incidents_service.repository.UserRepository;
import com.nullpointerexception.incidents_service.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/incidents")
@CrossOrigin(origins = "http://localhost:4200")
public class IncidentController {
    @Autowired
    private IncidentProducer incidentProducer;
    @Autowired
    private PowerPlantRepository powerPlantRepository;

    @Autowired
    private IncidentService incidentService;
    @Autowired
    private UserRepository userRepository;




    @PostMapping("/{plantId}")
    public ResponseEntity<?> createIncident(@PathVariable("plantId") Long plantId, @RequestBody IncidentDTO incidentDTO){
        System.out.println("COntroler.Incident called with incidentDTO: " + incidentDTO);
        //verifica daca nu primeste o valoare nula
        if(incidentDTO.getReportedBy() != null) {
            // Verifică dacă userul pentru reportedBy există sau creează unul nou
            User reportedByUser = userRepository.findByEmail(incidentDTO.getReportedBy().getEmail())
                    .orElseGet(() -> userRepository.save(incidentDTO.getReportedBy()));

            // Setează userii și powerPlant în DTO
            incidentDTO.setReportedBy(reportedByUser);
        }

        //verifica daca primeste o valoare nula
        if(incidentDTO.getAssignedTo() != null) {
            // Verifică dacă userul pentru assignedTo există sau creează unul nou
            User assignedToUser = userRepository.findByEmail(incidentDTO.getAssignedTo().getEmail())
                    .orElseGet(() -> userRepository.save(incidentDTO.getAssignedTo()));
            // Setează userii și powerPlant în DTO
            incidentDTO.setAssignedTo(assignedToUser);

        }

        // Validează câmpurile incidentDTO
        String validationResult = validateIncidentDTO(incidentDTO);
        if (!validationResult.equals("OK")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }



         Optional<PowerPlant> powerPlant = powerPlantRepository.findById(plantId);
        if (powerPlant.isPresent()) {
            incidentDTO.setPowerPlant(powerPlant.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
         if (incidentDTO.getPowerPlant() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PowerPlant field cannot be empty.");
        }

        // Crează incidentul
        Incident createdIncident = incidentService.createIncident(incidentDTO);
        // Trimite incidentul către Kafka producer
        incidentProducer.send(createdIncident);

        System.out.println("S-a creat.createdIncident: " + createdIncident);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdIncident);
    }

    private String validateIncidentDTO(IncidentDTO incidentDTO) {
        if (incidentDTO.getDescription() == null || incidentDTO.getDescription().trim().isEmpty()) {
            return "Description field cannot be empty.";
        }
        if (incidentDTO.getSeverity() == null || incidentDTO.getSeverity().trim().isEmpty()) {
            return "Severity field cannot be empty.";
        }
        if (incidentDTO.getStatus() == null || incidentDTO.getStatus().trim().isEmpty()) {
            return "Status field cannot be empty.";
        }

        if (incidentDTO.getStatus().equals("Closed") && (incidentDTO.getResolution() == null || incidentDTO.getResolution().trim().isEmpty())) {
            return "Resolution field cannot be empty when the status is Closed.";
        }
        return "OK";
    }

}