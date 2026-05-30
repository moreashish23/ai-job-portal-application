package com.portal.job.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateCoverLetterRequest {

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Candidate name is required")
    private String candidateName;

    // Candidate's skills and experience summary
    @NotBlank(message = "Candidate experience summary is required")
    @Size(max = 2000, message = "Experience summary must not exceed 2000 characters")
    private String candidateExperienceSummary;

    private List<String> candidateSkills;

    // Optional tone preference
    // e.g. "professional", "enthusiastic", "concise"
    private String tone;
}