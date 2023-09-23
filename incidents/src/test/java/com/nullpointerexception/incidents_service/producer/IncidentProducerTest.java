package com.nullpointerexception.incidents_service.producer;

import com.nullpointerexception.incidents_service.model.Incident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

public class IncidentProducerTest {

    @Mock
    private KafkaTemplate<String, Incident> kafkaTemplate;

    private IncidentProducer incidentProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        incidentProducer = new IncidentProducer(kafkaTemplate);
    }

    @Test
    void testSend() {
        Incident incident = new Incident();
        incident.setDescription("Test Incident");
        incident.setStatus("Open");
        incident.setSeverity("High");

        incidentProducer.send(incident);

        verify(kafkaTemplate).send("incidents", incident);
    }
}
