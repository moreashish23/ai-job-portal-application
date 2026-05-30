package com.portal.job.feign;

import com.portal.job.dto.response.ResumeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "job-portal-resume-service")
public interface ResumeServiceClient {

    @GetMapping("/api/resumes/{resumeId}")
    ResumeResponse getResumeById(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long userId
    );
}