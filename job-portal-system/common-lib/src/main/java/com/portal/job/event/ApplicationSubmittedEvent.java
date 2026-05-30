package com.portal.job.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationSubmittedEvent {

    private Long applicationId;
    private Long candidateId;
    private Long jobId;
    private Long employerId;
    private Long companyId;

    // Job details — denormalized at publish time so consumer needs no extra Feign calls
    private String jobTitle;
    private String companyName;
}