package com.portal.job.service.impl;

import com.portal.job.dto.response.JobResponse;
import com.portal.job.exception.BadRequestException;
import com.portal.job.exception.ResourceNotFoundException;
import com.portal.job.feign.JobServiceClient;
import com.portal.job.mapper.PreferenceMapper;
import com.portal.job.modal.SavedJob;
import com.portal.job.payload.SaveJobRequest;
import com.portal.job.payload.SavedJobResponse;
import com.portal.job.repository.SavedJobRepository;
import com.portal.job.service.SavedJobService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobServiceClient jobServiceClient;

    private static final int MAX_SAVED_JOBS = 500;

    @Override
    @Transactional
    public SavedJobResponse saveJob(Long candidateId, SaveJobRequest request) {

        if (savedJobRepository.existsByCandidateIdAndJobId(candidateId, request.getJobId())) {
            throw new BadRequestException("Job is already saved.");
        }

        long savedCount = savedJobRepository.countByCandidateId(candidateId);
        if (savedCount >= MAX_SAVED_JOBS) {
            throw new BadRequestException(
                    "Saved jobs limit reached (" + MAX_SAVED_JOBS + "). Please remove some before saving more.");
        }

        // Validate job existence — only reject on confirmed 404, not on infra errors
        JobResponse job = null;
        try {
            job = jobServiceClient.getJobById(request.getJobId());
        } catch (FeignException.NotFound e) {
            // Job-service confirmed the job does not exist → hard reject
            throw new ResourceNotFoundException("Job", request.getJobId());
        } catch (Exception e) {
            // job-service temporarily unavailable — allow save to proceed
            // The job reference is stored by ID; integrity is maintained via DB
            log.warn("Could not verify job {} via Feign (service may be down): {}", request.getJobId(), e.getMessage());
        }

        SavedJob savedJob = SavedJob.builder()
                .candidateId(candidateId)
                .jobId(request.getJobId())
                .note(request.getNote())
                .build();

        SavedJob persisted = savedJobRepository.save(savedJob);
        log.info("Candidate {} saved job {}", candidateId, request.getJobId());

        SavedJobResponse response = PreferenceMapper.toSavedJobResponse(persisted);
        if (job != null) {
            response.setJob(job);
        }
        return response;
    }

    @Override
    public Page<SavedJobResponse> getSavedJobs(Long candidateId, Pageable pageable) {
        return savedJobRepository
                .findByCandidateIdOrderBySavedAtDesc(candidateId, pageable)
                .map(saved -> {
                    SavedJobResponse response = PreferenceMapper.toSavedJobResponse(saved);
                    enrichWithJob(response);
                    return response;
                });
    }

    @Override
    public boolean isJobSaved(Long candidateId, Long jobId) {
        return savedJobRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }

    @Override
    @Transactional
    public void unsaveJob(Long candidateId, Long jobId) {
        if (!savedJobRepository.existsByCandidateIdAndJobId(candidateId, jobId)) {
            throw new ResourceNotFoundException("Saved job not found for job id: " + jobId);
        }
        savedJobRepository.deleteByCandidateIdAndJobId(candidateId, jobId);
        log.info("Candidate {} unsaved job {}", candidateId, jobId);
    }

    private void enrichWithJob(SavedJobResponse response) {
        try {
            JobResponse job = jobServiceClient.getJobById(response.getJobId());
            response.setJob(job);
        } catch (Exception e) {
            log.warn("Could not enrich saved job {} with job details: {}", response.getJobId(), e.getMessage());
        }
    }
}