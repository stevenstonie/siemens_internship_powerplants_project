package com.example.demo.controller;

import com.example.demo.model.PowerPlant;
import com.example.demo.repository.PowerPlantRepository;
import com.example.demo.service.PowerPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/powerplants")
@CrossOrigin(origins = "http://localhost:4200")
public class PowerPlantController {
    @Autowired
    private PowerPlantRepository powerPlantRepository;
    @Autowired
    private PowerPlantService powerPlantService;

    //GET powerPlants din Importer
    @GetMapping("/from-importer")
    public List<PowerPlant> getAllPowerPlantsFromImporter() {
        return powerPlantService.getAllPowerPlants();
    }

    @PostMapping("/start-production/{id}")
    public ResponseEntity<Void> startEnergyProduction(@PathVariable(value = "id") Long powerPlantId) {
        PowerPlant powerPlant = powerPlantRepository.findById(powerPlantId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Power Plant not found for this id :: " + powerPlantId));

        powerPlantService.startEnergyProduction(powerPlant);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop-production")
    public ResponseEntity<?> stopEnergyProduction() {
        powerPlantService.stopEnergyProduction();

        return ResponseEntity.ok("Energy production was stopped successfully.");
    }

    @GetMapping("/")
    public List<PowerPlant> getAllPowerPlants() {
        return powerPlantRepository.findAll();
    }

    // GET power plant by ID
    @GetMapping("/{id}")
    public ResponseEntity<PowerPlant> getPowerPlantById(@PathVariable(value = "id") Long powerPlantId)
            throws InvalidConfigurationPropertyValueException {
        PowerPlant powerPlant = powerPlantRepository.findById(powerPlantId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Power Plant not found for this id :: " + powerPlantId));
        return ResponseEntity.ok().body(powerPlant);
    }

    // CREATE a new power plant
    @PostMapping("/")
    public PowerPlant createPowerPlant(@RequestBody PowerPlant powerPlant) {
        return powerPlantRepository.save(powerPlant);
    }

    // UPDATE a power plant
    @PutMapping("/{id}")
    public ResponseEntity<PowerPlant> updatePowerPlant(@PathVariable(value = "id") Long powerPlantId,
                                                       @RequestBody PowerPlant powerPlantDetails) throws InvalidConfigurationPropertyValueException {
        PowerPlant powerPlant = powerPlantRepository.findById(powerPlantId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Power Plant not found for this id :: " + powerPlantId));

        powerPlant.setCountry(powerPlantDetails.getCountry());
        powerPlant.setCountry_long(powerPlantDetails.getCountry_long());
        powerPlant.setName(powerPlantDetails.getName());
        powerPlant.setGppd_idnr(powerPlantDetails.getGppd_idnr());
        powerPlant.setCapacity_mw(powerPlantDetails.getCapacity_mw());
        powerPlant.setLongitude(powerPlantDetails.getLongitude());
        powerPlant.setLatitude(powerPlantDetails.getLatitude());
        powerPlant.setPrimary_fuel(powerPlantDetails.getPrimary_fuel());

        final PowerPlant updatedPowerPlant = powerPlantRepository.save(powerPlant);
        return ResponseEntity.ok(updatedPowerPlant);
    }

    // DELETE a power plant
    @DeleteMapping("/{id}")
    public Map<String, Boolean> deletePowerPlant(@PathVariable(value = "id") Long powerPlantId)
            throws InvalidConfigurationPropertyValueException {
        PowerPlant powerPlant = powerPlantRepository.findById(powerPlantId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Power Plant not found for this id : " + powerPlantId));

        powerPlantRepository.delete(powerPlant);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    // DELETE all power plants
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteAllPowerPlants() {
        powerPlantRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
