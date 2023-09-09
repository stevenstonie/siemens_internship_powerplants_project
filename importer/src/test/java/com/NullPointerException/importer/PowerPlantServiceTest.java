package com.NullPointerException.importer;

import com.NullPointerException.importer.model.PowerPlant;
import com.NullPointerException.importer.repository.PowerPlantRepository;
import com.NullPointerException.importer.service.PowerPlantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class PowerPlantServiceTest {
@Autowired
    private PowerPlantService powerPlantService;
    @Autowired
    private PowerPlantRepository powerPlantRepository;

    @Value("${importer.csv.filepath}")
    private String csvFilePath;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void testImportCSV_success() throws IOException {
        String validCsvFilePath = "src/test/resources/global_power_plant_database.csv";
        ReflectionTestUtils.setField(powerPlantService, "csvFilePath", validCsvFilePath);

        powerPlantService.importCSV();

        List<PowerPlant> savedPowerPlants = powerPlantRepository.findAll();
        assertEquals(34936, savedPowerPlants.size()); // fișierul CSV valid are 34936 randuri
    }
    @Test
    void testImportCSV_noNullColumns() throws IOException {
        String validCsvFilePath = "src/test/resources/global_power_plant_database.csv";
        ReflectionTestUtils.setField(powerPlantService, "csvFilePath", validCsvFilePath);

        powerPlantService.importCSV();

        List<PowerPlant> savedPowerPlants = powerPlantRepository.findAll();
        for (PowerPlant powerPlant : savedPowerPlants) {
            assertNotNull(powerPlant.getGppd_idnr(), "gppd_idnr should not be null");
            assertNotNull(powerPlant.getCountry(), "country should not be null");
            assertNotNull(powerPlant.getCountry_long(), "country_long should not be null");
            assertNotNull(powerPlant.getName(), "name should not be null");
            assertNotNull(powerPlant.getCapacity_mw(), "capacity_mw should not be null");
            assertNotNull(powerPlant.getLatitude(), "latitude should not be null");
            assertNotNull(powerPlant.getLongitude(), "longitude should not be null");
            assertNotNull(powerPlant.getPrimary_fuel(), "primary_fuel should not be null");
        }
    }



}//PowerPlantServiceTest
