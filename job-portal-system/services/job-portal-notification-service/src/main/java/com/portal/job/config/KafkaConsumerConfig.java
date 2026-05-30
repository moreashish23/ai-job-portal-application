package com.portal.job.config;

import com.portal.job.event.ApplicationStatusChangedEvent;
import com.portal.job.event.ApplicationSubmittedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    // ── Shared base consumer properties ────────────────────────────────────────

    private Map<String, Object> baseConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    // ── ApplicationSubmittedEvent consumer factory ─────────────────────────────

    @Bean
    public ConsumerFactory<String, ApplicationSubmittedEvent> submittedConsumerFactory() {
        JsonDeserializer<ApplicationSubmittedEvent> deserializer =
                new JsonDeserializer<>(ApplicationSubmittedEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApplicationSubmittedEvent>
    submittedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ApplicationSubmittedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(submittedConsumerFactory());
        factory.setCommonErrorHandler(kafkaErrorHandler());
        return factory;
    }

    // ── ApplicationStatusChangedEvent consumer factory ────────────────────────

    @Bean
    public ConsumerFactory<String, ApplicationStatusChangedEvent> statusChangedConsumerFactory() {
        JsonDeserializer<ApplicationStatusChangedEvent> deserializer =
                new JsonDeserializer<>(ApplicationStatusChangedEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApplicationStatusChangedEvent>
    statusChangedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ApplicationStatusChangedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(statusChangedConsumerFactory());
        factory.setCommonErrorHandler(kafkaErrorHandler());
        return factory;
    }

    // ── Error handler — retry 3 times with 2s gap, then skip ──────────────────
    // Prevents a single bad message from blocking the entire partition

    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        return new DefaultErrorHandler(
                (record, exception) -> {
                    // Dead-letter logging — in production replace with a DLQ topic
                    System.err.printf(
                            "[NOTIFICATION] Skipping unrecoverable message: topic=%s partition=%d offset=%d error=%s%n",
                            record.topic(), record.partition(), record.offset(), exception.getMessage()
                    );
                },
                new FixedBackOff(2000L, 3L)
        );
    }
}