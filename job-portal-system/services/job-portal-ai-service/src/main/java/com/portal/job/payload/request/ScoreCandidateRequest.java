package com.portal.job.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreCandidateRequest {

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    // Job context
    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    private List<String> jobRequiredSkills;

    @NotBlank(message = "Experience level is required")
    private String experienceLevel;

    // Candidate context — sourced from resume-service by application-service
    @NotBlank(message = "Candidate experience summary is required")
    private String candidateExperienceSummary;

    private List<String> candidateSkills;

    private String coverLetter;
}