package com.portal.job.controller;


import com.portal.job.dto.request.JobRequest;
import com.portal.job.dto.response.ApiResponse;
import com.portal.job.dto.response.JobResponse;
import com.portal.job.payload.JobSearchRequest;
import com.portal.job.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private  final JobService jobService;


    @PostMapping
    public ResponseEntity<JobResponse> createJob(

            @RequestHeader("X-User-Id")
            Long employerId,

            @RequestBody
            @Valid
            JobRequest jobRequest
    )  {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobService.createJob(employerId, jobRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(

            @PathVariable
            Long id
    )   {

        return ResponseEntity.ok(
                jobService.getJobById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> getJobs(@ModelAttribute JobSearchRequest req) {
        return ResponseEntity.ok(jobService.getJobs(req));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<JobResponse>> getJobsByCompany(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
                jobService.getJobsByCompany(companyId, PageRequest.of(page, size)));
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<JobResponse>> getAllJobsAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
                jobService.getAllJobsAdmin(PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(

            @PathVariable
            Long id,

            @RequestHeader("X-User-Id")
            Long employerId,

            @RequestBody
            @Valid
            JobRequest req

    )   {

        return ResponseEntity.ok(
                jobService.updateJob(id, employerId, req)
        );
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<JobResponse> publishJob(

            @PathVariable
            Long id,

            @RequestHeader("X-User-Id")
            Long employerId

    )  {

        return ResponseEntity.ok(
                jobService.publishJob(id, employerId)
        );
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<JobResponse> closeJob(

            @PathVariable
            Long id,

            @RequestHeader("X-User-Id")
            Long employerId

    )   {

        return ResponseEntity.ok(
                jobService.closeJob(id, employerId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteJob(

            @PathVariable
            Long id,

            @RequestHeader("X-User-Id")
            Long employerId

    )   {

        jobService.deleteJob(id, employerId);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Job deleted successfully",
                        true
                )
        );
    }

}
