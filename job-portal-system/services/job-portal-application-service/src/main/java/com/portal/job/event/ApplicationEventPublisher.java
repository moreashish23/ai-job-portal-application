package com.portal.job.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.application-submitted}")
    private String applicationSubmittedTopic;

    @Value("${kafka.topics.application-status-changed}")
    private String applicationStatusChangedTopic;

    public void publishApplicationSubmitted(ApplicationSubmittedEvent event) {
        try {
            kafkaTemplate.send(
                    applicationSubmittedTopic,
                    String.valueOf(event.getApplicationId()),
                    event
            );
            log.info("Published ApplicationSubmittedEvent — applicationId={}", event.getApplicationId());
        } catch (Exception e) {
            // Non-blocking — email notification failure must never break the apply flow
            log.error("Failed to publish ApplicationSubmittedEvent for applicationId={}: {}",
                    event.getApplicationId(), e.getMessage());
        }
    }

    public void publishApplicationStatusChanged(ApplicationStatusChangedEvent event) {
        try {
            kafkaTemplate.send(
                    applicationStatusChangedTopic,
                    String.valueOf(event.getApplicationId()),
                    event
            );
            log.info("Published ApplicationStatusChangedEvent — applicationId={} newStatus={}",
                    event.getApplicationId(), event.getNewStatus());
        } catch (Exception e) {
            log.error("Failed to publish ApplicationStatusChangedEvent for applicationId={}: {}",
                    event.getApplicationId(), e.getMessage());
        }
    }
}