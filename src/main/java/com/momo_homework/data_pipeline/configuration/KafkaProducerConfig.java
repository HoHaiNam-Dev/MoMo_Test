package com.momo_homework.data_pipeline.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getKeySerializer());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializer());
        configProps.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getRetries());
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getBatchSize());
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getLinger());
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.getBufferMemory());
        configProps.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getAcks());

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public NewTopic createTopic() {
        return TopicBuilder.name(kafkaProperties.getTopic())
                .partitions(5)
                .build();
    }
}