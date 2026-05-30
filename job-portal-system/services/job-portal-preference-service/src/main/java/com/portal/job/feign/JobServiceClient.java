package com.portal.job.feign;

import com.portal.job.dto.response.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-portal-job-service")
public interface JobServiceClient {

    @GetMapping("/api/jobs/{jobId}")
    JobResponse getJobById(@PathVariable Long jobId);
}