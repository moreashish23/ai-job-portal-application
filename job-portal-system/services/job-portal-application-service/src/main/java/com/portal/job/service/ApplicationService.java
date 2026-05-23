package com.portal.job.service;

import com.portal.job.modal.ApplicationStatus;
import com.portal.job.payload.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicationService {

    // Candidate: apply to a job
    ApplicationResponse applyToJob(Long candidateId, ApplyJobRequest request);

    // Candidate: get my applications (paginated)
    Page<ApplicationSummaryResponse> getMyApplications(Long candidateId, Pageable pageable);

    // Candidate: get my application for a specific job
    ApplicationResponse getMyApplicationForJob(Long candidateId, Long jobId);

    // Candidate: withdraw application
    void withdrawApplication(Long candidateId, Long applicationId);

    // Employer: get all applications for a job (paginated, optional status filter)
    Page<ApplicationSummaryResponse> getApplicationsForJob(Long employerId, Long jobId, ApplicationStatus status, Pageable pageable);

    // Employer: get full application detail
    ApplicationResponse getApplicationDetail(Long employerId, Long applicationId);

    // Employer: update application status
    ApplicationResponse updateApplicationStatus(Long employerId, Long applicationId, UpdateApplicationStatusRequest request);

    // Employer: toggle shortlist
    ApplicationResponse toggleShortlist(Long employerId, Long applicationId);

    // Employer: mark as viewed
    void markAsViewed(Long employerId, Long applicationId);

    // Employer: get shortlisted candidates for a job
    List<ApplicationSummaryResponse> getShortlistedForJob(Long employerId, Long jobId);
}