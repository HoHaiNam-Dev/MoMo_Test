package com.momo_homework.data_pipeline.service.kafka;

import java.util.List;

public interface KafkaService {
    void send(String userId, String line);

    void sendBatch(List<String> batch);
}
