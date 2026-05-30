package com.portal.job.service;

import com.portal.job.dto.request.JobRequest;
import com.portal.job.dto.response.JobResponse;
import com.portal.job.payload.JobSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {

    JobResponse createJob(Long employerId, JobRequest req) ;

    JobResponse getJobById(Long id);

    Page<JobResponse> getJobs(JobSearchRequest request);

    Page<JobResponse> getJobsByCompany(Long companyId, Pageable pageable);

    JobResponse updateJob(Long jobId, Long employerId, JobRequest req) ;

    JobResponse publishJob(Long jobId, Long employerId) ;

    JobResponse closeJob(Long jobId, Long employerId) ;

    void deleteJob(Long jobId, Long employerId) ;

    Page<JobResponse> getAllJobsAdmin(Pageable pageable);
}
