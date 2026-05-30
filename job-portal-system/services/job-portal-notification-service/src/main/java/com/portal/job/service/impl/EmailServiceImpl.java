package com.portal.job.service.impl;

import com.portal.job.event.ApplicationStatusChangedEvent;
import com.portal.job.event.ApplicationSubmittedEvent;
import com.portal.job.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from-address}")
    private String fromAddress;

    @Value("${app.mail.from-name}")
    private String fromName;

    // ── Candidate: application submitted ──────────────────────────────────────

    @Override
    @Async
    public void sendApplicationConfirmationToCandidate(
            ApplicationSubmittedEvent event,
            String candidateEmail,
            String candidateFullName
    ) {
        String subject = "Application Submitted Successfully — " + event.getJobTitle();

        String body = buildApplicationConfirmationBody(
                candidateFullName,
                event.getJobTitle(),
                event.getCompanyName(),
                event.getApplicationId()
        );

        sendEmail(candidateEmail, subject, body);
    }

    // ── Employer: new application received ────────────────────────────────────

    @Override
    @Async
    public void sendNewApplicationAlertToEmployer(
            ApplicationSubmittedEvent event,
            String employerEmail,
            String employerFullName,
            String candidateFullName
    ) {
        String subject = "New Application Received — " + event.getJobTitle();

        String body = buildNewApplicationBody(
                employerFullName,
                event.getJobTitle(),
                candidateFullName,
                event.getApplicationId()
        );

        sendEmail(employerEmail, subject, body);
    }

    // ── Candidate: status changed ──────────────────────────────────────────────

    @Override
    @Async
    public void sendStatusUpdateToCandidate(
            ApplicationStatusChangedEvent event,
            String candidateEmail,
            String candidateFullName
    ) {
        String subject = "Your Application Status Has Been Updated — " + event.getJobTitle();

        String body = buildStatusUpdateBody(
                candidateFullName,
                event.getJobTitle(),
                event.getCompanyName(),
                event.getPreviousStatus(),
                event.getNewStatus(),
                event.getApplicationId()
        );

        sendEmail(candidateEmail, subject, body);
    }

    // ── Core send method ──────────────────────────────────────────────────────

    private void sendEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);  // true = HTML content
            mailSender.send(message);
            log.info("Email sent — to={} subject={}", to, subject);
        } catch (MessagingException e) {
            log.error("MessagingException sending email to={} subject={} : {}", to, subject, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending email to={} : {}", to, e.getMessage());
        }
    }

    // ── Email body builders — pure Java strings, no template engine ───────────

    private String buildApplicationConfirmationBody(
            String candidateName, String jobTitle, String companyName, Long applicationId) {

        return "<html><body style='font-family:Arial,sans-serif;color:#374151;'>"
                + "<div style='max-width:580px;margin:auto;padding:24px;'>"
                + "<h2 style='color:#2563EB;'>Application Submitted </h2>"
                + "<p>Hi <strong>" + candidateName + "</strong>,</p>"
                + "<p>Your application has been successfully submitted. Here are the details:</p>"
                + "<table style='background:#F3F4F6;border-radius:6px;padding:16px;width:100%;border-collapse:collapse;'>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Position</td><td style='padding:6px;'>" + jobTitle + "</td></tr>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Company</td><td style='padding:6px;'>" + companyName + "</td></tr>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Application ID</td><td style='padding:6px;'>#" + applicationId + "</td></tr>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Status</td><td style='padding:6px;'>Pending Review</td></tr>"
                + "</table>"
                + "<p style='margin-top:20px;'>The employer will review your application shortly. "
                + "You can track your application status from your dashboard at any time.</p>"
                + "<p>Good luck! </p>"
                + "<hr style='border:none;border-top:1px solid #E5E7EB;margin-top:24px;'/>"
                + "<p style='font-size:12px;color:#9CA3AF;'>You received this email because you applied via AI Job Portal.</p>"
                + "</div></body></html>";
    }

    private String buildNewApplicationBody(
            String employerName, String jobTitle, String candidateName, Long applicationId) {

        return "<html><body style='font-family:Arial,sans-serif;color:#374151;'>"
                + "<div style='max-width:580px;margin:auto;padding:24px;'>"
                + "<h2 style='color:#059669;'>New Application Received </h2>"
                + "<p>Hi <strong>" + employerName + "</strong>,</p>"
                + "<p>You have received a new application for one of your job postings.</p>"
                + "<table style='background:#F3F4F6;border-radius:6px;padding:16px;width:100%;border-collapse:collapse;'>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Position</td><td style='padding:6px;'>" + jobTitle + "</td></tr>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Candidate</td><td style='padding:6px;'>" + candidateName + "</td></tr>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Application ID</td><td style='padding:6px;'>#" + applicationId + "</td></tr>"
                + "</table>"
                + "<p style='margin-top:20px;'>Log in to your employer dashboard to review the application and resume.</p>"
                + "<hr style='border:none;border-top:1px solid #E5E7EB;margin-top:24px;'/>"
                + "<p style='font-size:12px;color:#9CA3AF;'>You received this email because you are a registered employer on AI Job Portal.</p>"
                + "</div></body></html>";
    }

    private String buildStatusUpdateBody(
            String candidateName, String jobTitle, String companyName,
            String previousStatus, String newStatus, Long applicationId) {

        String statusColor = resolveStatusColor(newStatus);
        String statusMessage = resolveStatusMessage(newStatus);

        return "<html><body style='font-family:Arial,sans-serif;color:#374151;'>"
                + "<div style='max-width:580px;margin:auto;padding:24px;'>"
                + "<h2 style='color:" + statusColor + ";'>Application Status Update</h2>"
                + "<p>Hi <strong>" + candidateName + "</strong>,</p>"
                + "<p>Your application status has been updated.</p>"
                + "<table style='background:#F3F4F6;border-radius:6px;padding:16px;width:100%;border-collapse:collapse;'>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Position</td><td style='padding:6px;'>" + jobTitle + "</td></tr>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Company</td><td style='padding:6px;'>" + companyName + "</td></tr>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Application ID</td><td style='padding:6px;'>#" + applicationId + "</td></tr>"
                + "<tr><td style='padding:6px;font-weight:bold;'>Previous Status</td><td style='padding:6px;'>" + previousStatus + "</td></tr>"
                + "<tr><td style='padding:6px;font-weight:bold;'>New Status</td>"
                + "<td style='padding:6px;'><span style='background:" + statusColor + ";color:white;"
                + "padding:3px 10px;border-radius:10px;font-size:13px;'>" + newStatus + "</span></td></tr>"
                + "</table>"
                + "<p style='margin-top:20px;'>" + statusMessage + "</p>"
                + "<hr style='border:none;border-top:1px solid #E5E7EB;margin-top:24px;'/>"
                + "<p style='font-size:12px;color:#9CA3AF;'>You received this email because you applied via AI Job Portal.</p>"
                + "</div></body></html>";
    }

    private String resolveStatusColor(String status) {
        return switch (status) {
            case "SHORTLISTED"          -> "#2563EB";
            case "INTERVIEW_SCHEDULED"  -> "#7C3AED";
            case "OFFER_EXTENDED"       -> "#059669";
            case "HIRED"                -> "#065F46";
            case "REJECTED"             -> "#DC2626";
            case "WITHDRAWN"            -> "#6B7280";
            default                     -> "#374151";
        };
    }

    private String resolveStatusMessage(String status) {
        return switch (status) {
            case "SHORTLISTED"          -> "Congratulations! You have been shortlisted. The employer will be in touch soon.";
            case "INTERVIEW_SCHEDULED"  -> "An interview has been scheduled. Please check your dashboard for details.";
            case "OFFER_EXTENDED"       -> "Fantastic news — an offer has been extended to you! Log in to review it.";
            case "HIRED"                -> "Congratulations — you got the job! Welcome to your new role.";
            case "REJECTED"             -> "Thank you for your interest. The employer has decided to move forward with other candidates.";
            case "REVIEWED"             -> "Your application has been reviewed. Stay tuned for further updates.";
            default                     -> "Please log in to your dashboard to see the latest details.";
        };
    }
}