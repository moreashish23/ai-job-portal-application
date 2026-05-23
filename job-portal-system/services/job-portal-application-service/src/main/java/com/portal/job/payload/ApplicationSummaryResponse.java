package com.portal.job.payload;

import com.portal.job.modal.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationSummaryResponse {

    private Long id;
    private Long candidateId;
    private Long jobId;
    private Long companyId;
    private ApplicationStatus status;
    private Boolean viewed;
    private Boolean shortlisted;
    private Integer aiScore;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}