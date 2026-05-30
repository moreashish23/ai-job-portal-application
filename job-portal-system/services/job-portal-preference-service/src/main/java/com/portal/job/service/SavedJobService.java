package com.portal.job.service;

import com.portal.job.payload.SaveJobRequest;
import com.portal.job.payload.SavedJobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SavedJobService {

    SavedJobResponse saveJob(Long candidateId, SaveJobRequest request);

    Page<SavedJobResponse> getSavedJobs(Long candidateId, Pageable pageable);

    boolean isJobSaved(Long candidateId, Long jobId);

    void unsaveJob(Long candidateId, Long jobId);
}