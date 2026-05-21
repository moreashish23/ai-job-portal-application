package com.portal.job.dto.response;

import com.portal.job.domain.ResumeTemplate;
import com.portal.job.domain.ResumeVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResumeResponse {

    private Long id;
    private Long candidateId;
    private String title;
    private ResumeTemplate template;
    private ResumeVisibility visibility;
    private Boolean isDefault;
    private PersonalInfoResponse personalInfo;
    private String summary;
    private Integer completionScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //Todo:
    //
    // private List<WorkExperienceResponse> workExperiences;
    // private List<EducationResponse> educations;
    // private List<ResumeSkillResponse> skills;
    // private List<ProjectResponse> projects;
    // private List<CertificationResponse> certifications;
    // private List<AwardResponse> awards;
    // private List<LanguageResponse> languages;

}