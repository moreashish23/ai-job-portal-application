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
public class OptimizeResumeRequest {

    @NotBlank(message = "Current resume summary is required")
    @Size(max = 3000, message = "Resume summary must not exceed 3000 characters")
    private String currentSummary;

    private List<String> currentSkills;

    // Optional: target job for role-specific optimization
    private String targetJobTitle;

    private String targetExperienceLevel;
}