package com.example.alarm.adapter;

import com.example.alarm.config.KafkaProperties;
import com.example.alarm.domain.event.AlarmChanged;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AlarmProducerImpl implements AlarmProducer {

    private final Logger log = LoggerFactory.getLogger(AlarmProducerImpl.class);
    private static final String TOPIC_SCHEDULER = "topic_scheduler";

    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AlarmProducerImpl(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        log.info("Kafka producer initializing...");
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }

    @Override
    public void sendAlarmCreatedEvent(AlarmChanged alarmChanged) throws ExecutionException, InterruptedException, JsonProcessingException {
        String message = objectMapper.writeValueAsString(alarmChanged);
        producer.send(new ProducerRecord<>(TOPIC_SCHEDULER, message));
    }

    @Override
    public void sendAlarmDeletedEvent(AlarmChanged alarmChanged) throws ExecutionException, InterruptedException, JsonProcessingException {
        String message = objectMapper.writeValueAsString(alarmChanged);
        producer.send(new ProducerRecord<>(TOPIC_SCHEDULER, message));
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutdown Kafka producer");
        producer.close();
    }
}
