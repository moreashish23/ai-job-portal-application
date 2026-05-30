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
public class GenerateJobDescriptionRequest {

    @NotBlank(message = "Job title is required")
    @Size(max = 150, message = "Job title must not exceed 150 characters")
    private String jobTitle;

    @Size(max = 150, message = "Company name must not exceed 150 characters")
    private String companyName;

    // e.g. FULL_TIME, PART_TIME, CONTRACT
    private String jobType;

    // e.g. REMOTE, HYBRID, ON_SITE
    private String workMode;

    // e.g. JUNIOR, MID, SENIOR
    private String experienceLevel;

    private List<String> requiredSkills;

    @Size(max = 500, message = "Additional context must not exceed 500 characters")
    private String additionalContext;
}