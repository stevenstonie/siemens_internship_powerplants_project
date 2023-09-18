package com.example.demo.serviceTest;
import com.example.demo.kafka.KafkaProducer;
import com.example.demo.model.PowerPlant;
import com.example.demo.repository.PowerPlantRepository;
import com.example.demo.service.PowerPlantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

public class PowerPlantServiceTest {
    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private PowerPlantService powerPlantService;

    @Mock
    private PowerPlantRepository powerPlantRepository;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStartEnergyProduction() throws InterruptedException {
        PowerPlant testPlant = new PowerPlant("ROU", "Romania", "TestPlant",
                                        "1234", 500, 45.95, 24.97, "Coal");
        when(powerPlantRepository.findById(anyLong())).thenReturn(Optional.of(testPlant));

        powerPlantService.startEnergyProduction(testPlant);
        Thread.sleep(5000);
        verify(kafkaProducer, atLeastOnce()).sendMessage(anyString());
    }

    @Test
    public void testStopEnergyProduction() {
        PowerPlant testPlant = new PowerPlant("ROU", "Romania", "TestPlant",
                                        "1234", 500, 45.95, 24.97, "Coal");
        when(powerPlantRepository.findById(anyLong())).thenReturn(Optional.of(testPlant));

        powerPlantService.startEnergyProduction(testPlant);
        powerPlantService.stopEnergyProduction();
        // verify that no more messages are sent after stopping
        verifyNoMoreInteractions(kafkaProducer);
    }
}