package com.portal.job.repository;

import com.portal.job.modal.SavedJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    Optional<SavedJob> findByCandidateIdAndJobId(Long candidateId, Long jobId);

    Page<SavedJob> findByCandidateIdOrderBySavedAtDesc(Long candidateId, Pageable pageable);

    long countByCandidateId(Long candidateId);

    void deleteByCandidateIdAndJobId(Long candidateId, Long jobId);
}