package com.portal.job.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusChangedEvent {

    private Long applicationId;
    private Long candidateId;
    private Long employerId;
    private String previousStatus;
    private String newStatus;

    // Job details — denormalized
    private String jobTitle;
    private String companyName;
}