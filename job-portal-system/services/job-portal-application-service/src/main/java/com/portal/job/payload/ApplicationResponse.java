package com.portal.job.payload;

import com.portal.job.dto.response.JobResponse;
import com.portal.job.dto.response.ResumeResponse;
import com.portal.job.modal.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {

    private Long id;
    private Long candidateId;
    private Long jobId;
    private Long employerId;
    private Long companyId;
    private Long resumeId;
    private ApplicationStatus status;
    private String coverLetter;
    private Integer aiScore;
    private String aiScreeningSummary;
    private Boolean viewed;
    private Boolean shortlisted;
    private String additionalAnswers;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    // Enriched data (populated via Feign when available)
    private JobResponse job;
    private ResumeResponse resume;
}