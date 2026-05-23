package com.portal.job.repository;

import com.portal.job.modal.ApplicationStatus;
import com.portal.job.modal.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // Check if candidate already applied to a job
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    // Candidate: get my applications
    Page<JobApplication> findByCandidateId(Long candidateId, Pageable pageable);

    // Candidate: get application for a specific job
    Optional<JobApplication> findByCandidateIdAndJobId(Long candidateId, Long jobId);

    // Employer: get all applications for a job
    Page<JobApplication> findByJobId(Long jobId, Pageable pageable);

    // Employer: get applications for a job filtered by status
    Page<JobApplication> findByJobIdAndStatus(Long jobId, ApplicationStatus status, Pageable pageable);

    // Employer: get all applications for their company
    Page<JobApplication> findByEmployerId(Long employerId, Pageable pageable);

    // Employer: get shortlisted applications for a job
    List<JobApplication> findByJobIdAndShortlistedTrue(Long jobId);

    // Stats: count per status for a job
    long countByJobIdAndStatus(Long jobId, ApplicationStatus status);

    // Stats: count total applications for a job
    long countByJobId(Long jobId);
}