package com.momo_homework.data_pipeline.service.kafka.impl;

import com.momo_homework.data_pipeline.configuration.KafkaProperties;
import com.momo_homework.data_pipeline.service.kafka.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaServiceImpl implements KafkaService {
    private static final String DL = ","; // Assuming comma is the delimiter
    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Retryable(
            retryFor = {KafkaException.class, TimeoutException.class},
            backoff = @Backoff(delay = 2000)
    )
    public void send(String userId, String line) {
        String topic = kafkaProperties.getTopic();
        kafkaTemplate.send(topic, userId, line);
        log.info("Record sent to Kafka");
    }


    /**
     * Send a batch of records to Kafka
     * @param batch - List of records to send
     */
    @Override
    @Retryable(
            retryFor = {KafkaException.class, TimeoutException.class},
            backoff = @Backoff(delay = 2000)
    )
    public void sendBatch(List<String> batch) {
        String[] fields;
        String userId;
        String segment;
        String topic = kafkaProperties.getTopic();

        for (String line : batch) {
            fields = line.split(DL);

            // Skip if fields length is less than 2
            if (fields.length < 2) {
                log.info("Skipping line due to insufficient data: {}", line);
                continue;
            }

            userId = fields[0];
            segment = fields[1];

            // Skip if userId or segment is null or empty
            if (userId == null || userId.trim().isEmpty() || segment == null || segment.trim().isEmpty()) {
                log.info("Skipping line due to null or empty userId or segment: {}", line);
                continue;
            }

            // Send the record to Kafka
            kafkaTemplate.send(topic, userId, line);

        }
        log.info("Batch of {} records sent to Kafka", batch.size());
        batch.clear();

    }

}
