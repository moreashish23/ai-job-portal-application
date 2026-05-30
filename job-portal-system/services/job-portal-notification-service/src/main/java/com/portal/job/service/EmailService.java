package com.portal.job.service;

import com.portal.job.event.ApplicationStatusChangedEvent;
import com.portal.job.event.ApplicationSubmittedEvent;

public interface EmailService {

    void sendApplicationConfirmationToCandidate(
            ApplicationSubmittedEvent event,
            String candidateEmail,
            String candidateFullName
    );

    void sendNewApplicationAlertToEmployer(
            ApplicationSubmittedEvent event,
            String employerEmail,
            String employerFullName,
            String candidateFullName
    );

    void sendStatusUpdateToCandidate(
            ApplicationStatusChangedEvent event,
            String candidateEmail,
            String candidateFullName
    );
}