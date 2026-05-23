package com.portal.job.service.impl;

import com.portal.job.dto.response.JobResponse;
import com.portal.job.dto.response.ResumeResponse;
import com.portal.job.exception.BadRequestException;
import com.portal.job.exception.ForbiddenException;
import com.portal.job.exception.ResourceNotFoundException;
import com.portal.job.feign.JobServiceClient;
import com.portal.job.feign.ResumeServiceClient;
import com.portal.job.mapper.ApplicationMapper;
import com.portal.job.modal.ApplicationStatus;
import com.portal.job.modal.JobApplication;
import com.portal.job.payload.*;
import com.portal.job.repository.JobApplicationRepository;
import com.portal.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobServiceClient jobServiceClient;
    private final ResumeServiceClient resumeServiceClient;

    @Override
    public ApplicationResponse applyToJob(Long candidateId, ApplyJobRequest request) {

        // Check for duplicate application
        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, request.getJobId())) {
            throw new BadRequestException("You have already applied to this job.");
        }

        // Fetch job details via Feign to validate job exists and get employer/company info
        JobResponse job;
        try {
            job = jobServiceClient.getJobById(request.getJobId(), candidateId);
        } catch (Exception e) {
            log.error("Failed to fetch job {} from job-service: {}", request.getJobId(), e.getMessage());
            throw new ResourceNotFoundException("Job", request.getJobId());
        }

        // Validate resume ownership via Feign
        try {
            resumeServiceClient.getResumeById(request.getResumeId(), candidateId);
        } catch (Exception e) {
            log.error("Failed to fetch resume {} for candidate {}: {}", request.getResumeId(), candidateId, e.getMessage());
            throw new ResourceNotFoundException("Resume", request.getResumeId());
        }

        JobApplication application = JobApplication.builder()
                .candidateId(candidateId)
                .jobId(request.getJobId())
                .employerId(job.getEmployerId())
                .companyId(job.getCompanyId())
                .resumeId(request.getResumeId())
                .coverLetter(request.getCoverLetter())
                .additionalAnswers(request.getAdditionalAnswers())
                .status(ApplicationStatus.PENDING)
                .viewed(false)
                .shortlisted(false)
                .build();

        JobApplication saved = applicationRepository.save(application);

        log.info("Candidate {} applied to job {} — application id: {}", candidateId, request.getJobId(), saved.getId());

        ApplicationResponse response = ApplicationMapper.toResponse(saved);
        response.setJob(job);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationSummaryResponse> getMyApplications(Long candidateId, Pageable pageable) {
        return applicationRepository
                .findByCandidateId(candidateId, pageable)
                .map(ApplicationMapper::toSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getMyApplicationForJob(Long candidateId, Long jobId) {
        JobApplication app = applicationRepository
                .findByCandidateIdAndJobId(candidateId, jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found for this job."));

        ApplicationResponse response = ApplicationMapper.toResponse(app);
        enrichWithJobAndResume(response, candidateId);
        return response;
    }

    @Override
    public void withdrawApplication(Long candidateId, Long applicationId) {
        JobApplication app = getApplicationEntity(applicationId);
        assertCandidateOwner(app, candidateId);

        if (app.getStatus() == ApplicationStatus.HIRED || app.getStatus() == ApplicationStatus.REJECTED) {
            throw new BadRequestException("Cannot withdraw a " + app.getStatus().name().toLowerCase() + " application.");
        }

        app.setStatus(ApplicationStatus.WITHDRAWN);
        applicationRepository.save(app);
        log.info("Candidate {} withdrew application {}", candidateId, applicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationSummaryResponse> getApplicationsForJob(
            Long employerId, Long jobId, ApplicationStatus status, Pageable pageable) {

        if (status != null) {
            return applicationRepository
                    .findByJobIdAndStatus(jobId, status, pageable)
                    .map(ApplicationMapper::toSummary);
        }

        return applicationRepository
                .findByJobId(jobId, pageable)
                .map(ApplicationMapper::toSummary);
    }

    @Override
    @Transactional
    public ApplicationResponse getApplicationDetail(Long employerId, Long applicationId) {
        JobApplication app = getApplicationEntity(applicationId);
        assertEmployerOwner(app, employerId);

        // Auto-mark as viewed when employer opens it
        if (!app.getViewed()) {
            app.setViewed(true);
            applicationRepository.save(app);
        }

        ApplicationResponse response = ApplicationMapper.toResponse(app);
        enrichWithResume(response, app.getCandidateId());
        return response;
    }

    @Override
    public ApplicationResponse updateApplicationStatus(Long employerId, Long applicationId,
                                                       UpdateApplicationStatusRequest request) {
        JobApplication app = getApplicationEntity(applicationId);
        assertEmployerOwner(app, employerId);

        app.setStatus(request.getStatus());

        // Auto-shortlist when status is SHORTLISTED
        if (request.getStatus() == ApplicationStatus.SHORTLISTED) {
            app.setShortlisted(true);
        }

        JobApplication updated = applicationRepository.save(app);
        log.info("Employer {} updated application {} status to {}", employerId, applicationId, request.getStatus());
        return ApplicationMapper.toResponse(updated);
    }

    @Override
    public ApplicationResponse toggleShortlist(Long employerId, Long applicationId) {
        JobApplication app = getApplicationEntity(applicationId);
        assertEmployerOwner(app, employerId);

        app.setShortlisted(!app.getShortlisted());
        JobApplication updated = applicationRepository.save(app);
        return ApplicationMapper.toResponse(updated);
    }

    @Override
    public void markAsViewed(Long employerId, Long applicationId) {
        JobApplication app = getApplicationEntity(applicationId);
        assertEmployerOwner(app, employerId);
        app.setViewed(true);
        applicationRepository.save(app);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationSummaryResponse> getShortlistedForJob(Long employerId, Long jobId) {
        return applicationRepository
                .findByJobIdAndShortlistedTrue(jobId)
                .stream()
                .filter(app -> app.getEmployerId().equals(employerId))
                .map(ApplicationMapper::toSummary)
                .toList();
    }

    // ─── Private helpers ───────────────────────────────────────────────────────

    private JobApplication getApplicationEntity(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", applicationId));
    }

    private void assertCandidateOwner(JobApplication app, Long candidateId) {
        if (!app.getCandidateId().equals(candidateId)) {
            throw new ForbiddenException("You do not have access to this application.");
        }
    }

    private void assertEmployerOwner(JobApplication app, Long employerId) {
        if (!app.getEmployerId().equals(employerId)) {
            throw new ForbiddenException("You do not have access to this application.");
        }
    }

    private void enrichWithJobAndResume(ApplicationResponse response, Long userId) {
        try {
            JobResponse job = jobServiceClient.getJobById(response.getJobId(), userId);
            response.setJob(job);
        } catch (Exception e) {
            log.warn("Could not enrich job data for application {}: {}", response.getId(), e.getMessage());
        }
        enrichWithResume(response, userId);
    }

    private void enrichWithResume(ApplicationResponse response, Long candidateId) {
        try {
            ResumeResponse resume = resumeServiceClient.getResumeById(response.getResumeId(), candidateId);
            response.setResume(resume);
        } catch (Exception e) {
            log.warn("Could not enrich resume data for application {}: {}", response.getId(), e.getMessage());
        }
    }
}