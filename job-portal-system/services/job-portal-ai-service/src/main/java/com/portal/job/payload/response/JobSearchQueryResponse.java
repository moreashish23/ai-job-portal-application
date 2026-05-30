package com.portal.job.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSearchQueryResponse {

    // The original natural language query
    private String originalQuery;

    // Extracted structured fields that can be passed to job-service search
    private String keyword;
    private String location;
    private String jobType;
    private String workMode;
    private String experienceLevel;

    private boolean success;
}