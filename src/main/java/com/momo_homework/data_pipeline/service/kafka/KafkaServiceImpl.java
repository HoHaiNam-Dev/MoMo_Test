package com.momo_homework.data_pipeline.service.kafka;

import com.momo_homework.data_pipeline.configuration.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaServiceImpl implements KafkaService {
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
    }
}
