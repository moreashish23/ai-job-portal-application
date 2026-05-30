package com.portal.job.payload;

import com.portal.job.domain.ExperienceLevel;
import com.portal.job.domain.JobType;
import com.portal.job.domain.WorkMode;
import com.portal.job.modal.AlertFrequency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAlertResponse {

    private Long id;
    private Long candidateId;
    private String alertName;
    private String keyword;
    private String location;
    private JobType jobType;
    private WorkMode workMode;
    private ExperienceLevel experienceLevel;
    private Long categoryId;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private AlertFrequency frequency;
    private Boolean isActive;
    private LocalDateTime lastTriggeredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}