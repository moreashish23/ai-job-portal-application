package com.portal.job.payload;


import com.portal.job.domain.ExperienceLevel;
import com.portal.job.domain.JobStatus;
import com.portal.job.domain.JobType;
import com.portal.job.domain.WorkMode;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSearchRequest {

    private String keyword;

    private Long categoryId;

    private List<Long> skillIds;

    private List<Long> tagIds;

    private Long companyId;

    /** Matches city, state, or country (case-insensitive LIKE). */
    private String location;

    /** Salary overlap — job's max salary must be >= minSalary. */
    private BigDecimal minSalary;

    /** Salary overlap — job's min salary must be <= maxSalary. */
    private BigDecimal maxSalary;

    private JobType jobType;

    private WorkMode workMode;

    private ExperienceLevel experienceLevel;

    /** Defaults to OPEN in the service when null. */
    private JobStatus status;

    private Integer minOpenings;

    private Integer maxOpenings;

    private int page = 0;
    private int size = 20;
    private String sortBy = "createdAt";
    private String sortDir = "desc";
}