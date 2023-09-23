package com.nullpointerexception.incidents_service.producer;

import com.nullpointerexception.incidents_service.model.Incident;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class IncidentProducer {
    private final KafkaTemplate<String, Incident> kafkaTemplate;

    public IncidentProducer(KafkaTemplate<String, Incident> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Incident incident) {
        kafkaTemplate.send("incidents", incident);
    }
}
