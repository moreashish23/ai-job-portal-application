package com.portal.job.payload;

import com.portal.job.dto.response.JobResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJobResponse {

    private Long id;
    private Long candidateId;
    private Long jobId;
    private String note;
    private LocalDateTime savedAt;

    // Enriched via Feign — may be null if job-service is unavailable
    private JobResponse job;
}