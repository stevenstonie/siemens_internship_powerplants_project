package com.example.consumer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class IncidentListener {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(IncidentListener.class);


    @Autowired
    public IncidentListener(SimpMessagingTemplate simpMessagingTemplate, ObjectMapper objectMapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${incidents.topic}",
            groupId = "group_id",
            containerFactory = "incidentKafkaListenerContainerFactory"
    )
    public void consumeIncident(Incident incident) {
        try {
            logger.info("Received incident: " + incident);
            Map<String, Object> message  = new HashMap<>();
            message .put("powerPlantId", incident.getPowerPlant().getId());
            message .put("severity", incident.getSeverity());
            simpMessagingTemplate.convertAndSend("/topic/incidents", incident );
        } catch (Exception e) {
            logger.error("Error sending data through WebSocket: ", e);
        }
    }



}//Incidents






