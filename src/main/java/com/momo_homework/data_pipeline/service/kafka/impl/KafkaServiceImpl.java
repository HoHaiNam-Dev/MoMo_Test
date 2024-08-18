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
        try {
            for (String line : batch) {
                fields = line.split(DL);

                if (fields.length < 2) {
                    log.info("Skipping line due to insufficient data: {}", line);
                    continue;
                }

                userId = fields[0];
                segment = fields[1];

                if (userId == null || userId.trim().isEmpty() || segment == null || segment.trim().isEmpty()) {
                    log.info("Skipping line due to null or empty userId or segment: {}", line);
                    continue;
                }

                kafkaTemplate.send(topic, userId, line);
            }
            log.info("Batch of {} records sent to Kafka", batch.size());
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("ArrayIndexOutOfBoundsException: Malformed data in batch: {}", e.getMessage());
        } catch (KafkaException e) {
            log.error("KafkaException: Error sending batch to Kafka: {}", e.getMessage());
            // Consider adding retry logic here if needed
        } catch (NullPointerException e) {
            log.error("NullPointerException: Unexpected null value encountered: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending batch to Kafka: {}", e.getMessage());
        }

    }
}
