package com.portal.job.repository;

import com.portal.job.modal.AlertFrequency;
import com.portal.job.modal.JobAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobAlertRepository extends JpaRepository<JobAlert, Long> {

    Page<JobAlert> findByCandidateIdOrderByCreatedAtDesc(Long candidateId, Pageable pageable);

    Optional<JobAlert> findByIdAndCandidateId(Long id, Long candidateId);

    long countByCandidateIdAndIsActiveTrue(Long candidateId);

    // Used by the Notification Service scheduler to find alerts to trigger
    List<JobAlert> findByIsActiveTrueAndFrequency(AlertFrequency frequency);
}