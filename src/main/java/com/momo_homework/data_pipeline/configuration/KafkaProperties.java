package com.momo_homework.data_pipeline.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaProperties {
    private String topic;
    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private int retries;
    private int batchSize;
    private int linger;
    private long bufferMemory;
    private String acks;
}