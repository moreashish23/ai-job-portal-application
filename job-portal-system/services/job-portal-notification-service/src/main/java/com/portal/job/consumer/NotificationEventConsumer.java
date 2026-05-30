package com.portal.job.consumer;

import com.portal.job.dto.response.UserResponse;
import com.portal.job.event.ApplicationStatusChangedEvent;
import com.portal.job.event.ApplicationSubmittedEvent;
import com.portal.job.feign.UserServiceClient;
import com.portal.job.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final EmailService emailService;
    private final UserServiceClient userServiceClient;

    // ── Consumer: ApplicationSubmittedEvent ────────────────────────────────────

    @KafkaListener(
            topics = "${kafka.topics.application-submitted}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "submittedKafkaListenerContainerFactory"
    )
    public void onApplicationSubmitted(
            @Payload ApplicationSubmittedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("Consumed ApplicationSubmittedEvent — applicationId={} topic={} partition={} offset={}",
                event.getApplicationId(), topic, partition, offset);

        // Fetch candidate details from user-service
        UserResponse candidate = fetchUser(event.getCandidateId());
        if (candidate == null) {
            log.warn("Cannot send candidate email — user not found for candidateId={}", event.getCandidateId());
            return;
        }

        // 1. Email to candidate — confirm their application
        emailService.sendApplicationConfirmationToCandidate(
                event,
                candidate.getEmail(),
                candidate.getFullName()
        );

        // 2. Email to employer — notify of new applicant
        UserResponse employer = fetchUser(event.getEmployerId());
        if (employer != null) {
            emailService.sendNewApplicationAlertToEmployer(
                    event,
                    employer.getEmail(),
                    employer.getFullName(),
                    candidate.getFullName()
            );
        } else {
            log.warn("Employer not found for employerId={} — skipping employer notification",
                    event.getEmployerId());
        }
    }

    // ── Consumer: ApplicationStatusChangedEvent ────────────────────────────────

    @KafkaListener(
            topics = "${kafka.topics.application-status-changed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "statusChangedKafkaListenerContainerFactory"
    )
    public void onApplicationStatusChanged(
            @Payload ApplicationStatusChangedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("Consumed ApplicationStatusChangedEvent — applicationId={} newStatus={} topic={} offset={}",
                event.getApplicationId(), event.getNewStatus(), topic, offset);

        // No email on WITHDRAWN — candidate initiated it themselves
        if ("WITHDRAWN".equals(event.getNewStatus())) {
            log.info("Status is WITHDRAWN — skipping email for applicationId={}", event.getApplicationId());
            return;
        }

        UserResponse candidate = fetchUser(event.getCandidateId());
        if (candidate == null) {
            log.warn("Cannot send status email — user not found for candidateId={}", event.getCandidateId());
            return;
        }

        emailService.sendStatusUpdateToCandidate(
                event,
                candidate.getEmail(),
                candidate.getFullName()
        );
    }

    // ── Private helper ─────────────────────────────────────────────────────────

    private UserResponse fetchUser(Long userId) {
        try {
            return userServiceClient.getUserById(userId);
        } catch (Exception e) {
            log.error("Failed to fetch user userId={} from user-service: {}", userId, e.getMessage());
            return null;
        }
    }
}