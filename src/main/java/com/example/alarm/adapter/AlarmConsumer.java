package com.example.alarm.adapter;

import com.example.alarm.config.KafkaProperties;
import com.example.alarm.domain.event.NoticeChanged;
import com.example.alarm.service.NoticeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AlarmConsumer {

    private final Logger log = LoggerFactory.getLogger(AlarmConsumer.class);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    public static final String TOPIC_ALARM = "topic_alarm";
    private final KafkaProperties kafkaProperties;
    private KafkaConsumer<String, String> kafkaConsumer;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private NoticeService noticeService;

    public AlarmConsumer(KafkaProperties kafkaProperties, NoticeService noticeService) {
        this.kafkaProperties = kafkaProperties;
        this.noticeService = noticeService;
    }

    @PostConstruct
    public void start() {
        log.info("Kafka consumer starting ...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        kafkaConsumer.subscribe(Collections.singleton(TOPIC_ALARM));
        log.info("Kafka consumer started");

        executorService.execute(() -> {
            try {
                while (!closed.get()) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                    for (ConsumerRecord<String, String> record : records) {
                        log.info("Consumed message in {} : {}", TOPIC_ALARM, record.value());
                        ObjectMapper objectMapper = new ObjectMapper();
                        NoticeChanged noticeChanged = objectMapper.readValue(record.value(), NoticeChanged.class);
                        noticeService.processNoticeChanged(noticeChanged);
                    }
                }
                kafkaConsumer.commitSync();
            } catch (WakeupException e) {
                if (!closed.get()) {
                    throw e;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                log.info("kafka consumer close");
                kafkaConsumer.close();
            }
        });
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}
