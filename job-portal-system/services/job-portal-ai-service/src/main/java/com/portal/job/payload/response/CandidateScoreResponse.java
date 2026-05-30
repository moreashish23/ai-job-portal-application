package com.portal.job.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateScoreResponse {

    private Long applicationId;

    // Overall AI score 0-100
    private Integer score;

    // AI explanation of the score
    private String summary;

    // Matched and missing skills
    private String matchedSkills;
    private String missingSkills;

    // Hiring recommendation
    private String recommendation;

    private boolean success;
}