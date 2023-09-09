package com.NullPointerException.importer.controller;

import com.NullPointerException.importer.model.PowerPlant;
import com.NullPointerException.importer.service.PowerPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/powerplants")
public class PowerPlantController {

    @Autowired
    private PowerPlantService powerPlantService;

    @Value("${importer.csv.filepath}")
    private String csvFilePath;

    @PostMapping("/import")
    public ResponseEntity<String> importCSV() {
        try {
            powerPlantService.importCSV();
            return ResponseEntity.ok("Hello world!^_^\n" + "CSV data imported successfully!^_^ ");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error importing CSV data *-_-: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PowerPlant>> getAllPowerPlants() {
        List<PowerPlant> powerPlants = powerPlantService.getAllPowerPlants();
        return ResponseEntity.ok(powerPlants);
    }


}
