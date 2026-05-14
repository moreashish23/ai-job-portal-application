package com.portal.job.mapper;

import
import com.portal.job.dto.response.CompanyResponse;
import com.portal.job.dto.response.JobResponse;
import com.portal.job.modal.Job;
import com.portal.job.modal.embeddable.JobLocation;
import com.portal.job.modal.embeddable.SalaryRange;

public class JobMapper {

    public static JobResponse toResponse(Job job, CompanyResponse companyResponse) {

        JobLocation loc = job.getLocation();
        SalaryRange sal = job.getSalaryRange();



        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .responsibilities(job.getResponsibilities())
                .benefits(job.getBenefits())

                .company(companyResponse)

//              .category(toCategoryResponse(job.getCategory()))
//              .skills(skills)
//              .tags(tags)

                // location
                .address(loc != null ? loc.getAddress() : null)
                .city(loc != null ? loc.getCity() : null)
                .state(loc != null ? loc.getState() : null)
                .country(loc != null ? loc.getCountry() : null)
                .zipCode(loc != null ? loc.getZipCode() : null)

                // salary
                .minSalary(sal != null ? sal.getMinSalary() : null)
                .maxSalary(sal != null ? sal.getMaxSalary() : null)

                // classification
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .experienceLevel(job.getExperienceLevel())
                .status(job.getStatus())

                // posting
                .openings(job.getOpenings())
                .applicationDeadline(job.getApplicationDeadline())
                .expiresAt(job.getExpiresAt())
                .active(job.getActive())

                // timestamps
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .publishedAt(job.getPublishedAt())
                .closedAt(job.getClosedAt())

                .build();
    }

}
