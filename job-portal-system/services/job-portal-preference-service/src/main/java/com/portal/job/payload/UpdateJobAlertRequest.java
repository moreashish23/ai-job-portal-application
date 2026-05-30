package com.portal.job.payload;

import com.portal.job.domain.ExperienceLevel;
import com.portal.job.domain.JobType;
import com.portal.job.domain.WorkMode;
import com.portal.job.modal.AlertFrequency;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateJobAlertRequest {

    @Size(max = 100, message = "Alert name must not exceed 100 characters")
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
}