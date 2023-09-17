package com.example.consumer.model;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EnergyProductionListener {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public EnergyProductionListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = {"${kafka.topic.energy-production}"},
            groupId = "myGroup"
    )
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
