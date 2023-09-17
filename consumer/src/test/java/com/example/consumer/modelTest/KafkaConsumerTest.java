//package com.example.consumer.modelTest;
//
//import com.example.consumer.model.Incident;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.Duration;
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//
//public class KafkaConsumerTest {
//
//    @Autowired
//    private KafkaTemplate<String, Incident> kafkaTemplate;
//
//    @Autowired
//    private KafkaConsumer<String, Incident> kafkaConsumer;
//
//    @Test
//    public void testReceive() throws InterruptedException {
//        String topic = "incidents";
//        Incident incident = new Incident();
//        // Set incident fields here
//        // ...
//        kafkaTemplate.send(topic, incident);
//
//        kafkaConsumer.subscribe(Collections.singletonList(topic));
//        ConsumerRecords<String, Incident> records = kafkaConsumer.poll(Duration.ofMillis(10000));
//        assertFalse(records.isEmpty());
//
//        ConsumerRecord<String, Incident> record = records.iterator().next();
//        assertEquals(incident, record.value());
//    }
//}
