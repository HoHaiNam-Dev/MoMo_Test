package com.momo_homework.data_pipeline.service.kafka;

public interface KafkaService {
    void send(String userId, String line);
}
