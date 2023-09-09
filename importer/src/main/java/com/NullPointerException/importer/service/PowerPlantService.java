package com.NullPointerException.importer.service;

import com.NullPointerException.importer.model.PowerPlant;
import com.NullPointerException.importer.repository.PowerPlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PowerPlantService {

    @Autowired
    private PowerPlantRepository powerPlantRepository;
    @Value("${importer.csv.filepath}")
    private String csvFilePath;

    public void importCSV() throws IOException {
        try (FileReader fileReader = new FileReader(csvFilePath)) {
            List<PowerPlant> powerPlants = new CsvToBeanBuilder<PowerPlant>(fileReader)
                    .withType(PowerPlant.class)
                    .build()
                    .parse()
                    .stream()
                    .filter(this::isValidPowerPlant)
                    .collect(Collectors.toList());

            powerPlantRepository.saveAll(powerPlants);
        }
    }

    private boolean isValidPowerPlant(PowerPlant powerPlant) {
        return powerPlant.getGppd_idnr() != null && powerPlant.getCountry() != null &&

                powerPlant.getCountry_long() != null && powerPlant.getName() != null &&
                powerPlant.getCapacity_mw() != null && powerPlant.getLatitude() != null &&
                powerPlant.getLongitude() != null && powerPlant.getPrimary_fuel() != null;
    }

    public List<PowerPlant> getAllPowerPlants() {
        return powerPlantRepository.findAll();
    }
}
