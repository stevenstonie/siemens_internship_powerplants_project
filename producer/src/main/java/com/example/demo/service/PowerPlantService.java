package com.example.demo.service;

import com.example.demo.kafka.KafkaProducer;
import com.example.demo.model.PowerPlant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PowerPlantService {
    @Value("${importer.service.url}")
    private String importerServiceUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private KafkaProducer kafkaProducer;
    private AtomicReference<ScheduledExecutorService> currentScheduler = new AtomicReference<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(PowerPlantService.class);


    public List<PowerPlant> powerPlantsFetchFromImporter() {
        ResponseEntity<List<PowerPlant>> response = restTemplate.exchange(
                importerServiceUrl + "/powerplants/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PowerPlant>>() {
                });
        return response.getBody();
    }

    //comunicarea cu Importer.
    public List<PowerPlant> getAllPowerPlants() {
        return powerPlantsFetchFromImporter();
    }


    public void startEnergyProduction(PowerPlant powerPlant) {
        // opreste  producția de energie curentă, dacă există
        ScheduledExecutorService oldScheduler = currentScheduler.getAndSet(null);
        if (oldScheduler != null) {
            oldScheduler.shutdown();
        }
        // porneste producția de energie pt powerPlant cu ID-ul dat
        ScheduledExecutorService newScheduler = Executors.newSingleThreadScheduledExecutor();
        newScheduler.scheduleAtFixedRate(() -> {
            double energyProduction = generateRandomEnergyProduction();
            kafkaProducer.sendMessage(String.format("Power Plant %s produced %f MW", powerPlant.getName(), energyProduction));
        }, 0, 2, TimeUnit.SECONDS);

        // se actualizeaza scheduler-ul curent
        currentScheduler.set(newScheduler);
    }

    public void stopEnergyProduction() {
        ScheduledExecutorService scheduler = currentScheduler.getAndSet(null);

        if (scheduler != null) {
            scheduler.shutdown();
        }
        LOGGER.info("Energy production was stopped successfully.");

    }

    public double generateRandomEnergyProduction() {
        return ThreadLocalRandom.current().nextDouble(0, 1000);
    }


}
