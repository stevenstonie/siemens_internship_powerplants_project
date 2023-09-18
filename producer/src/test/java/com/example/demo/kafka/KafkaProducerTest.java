package com.example.demo.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

public class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaProducer = new KafkaProducer();
        kafkaProducer.setKafkaTemplate(kafkaTemplate);
        kafkaProducer.setTopicName("energy-production");

    }

    @Test
    void testSendMessage() {
        String message = "Test message";
        kafkaProducer.sendMessage(message);

        verify(kafkaTemplate).send(anyString(), eq(message));
    }
}

