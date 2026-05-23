package com.portal.job.controller;

import com.portal.job.dto.response.ApiResponse;
import com.portal.job.modal.ApplicationStatus;
import com.portal.job.payload.*;
import com.portal.job.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // ─── Candidate APIs ────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ApplicationResponse> applyToJob(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid ApplyJobRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicationService.applyToJob(candidateId, request));
    }

    @GetMapping("/my")
    public ResponseEntity<Page<ApplicationSummaryResponse>> getMyApplications(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(applicationService.getMyApplications(candidateId, pageable));
    }

    @GetMapping("/my/job/{jobId}")
    public ResponseEntity<ApplicationResponse> getMyApplicationForJob(
            @RequestHeader("X-User-Id") Long candidateId,
            @PathVariable Long jobId
    ) {
        return ResponseEntity.ok(applicationService.getMyApplicationForJob(candidateId, jobId));
    }

    @PatchMapping("/{applicationId}/withdraw")
    public ResponseEntity<ApiResponse> withdrawApplication(
            @RequestHeader("X-User-Id") Long candidateId,
            @PathVariable Long applicationId
    ) {
        applicationService.withdrawApplication(candidateId, applicationId);
        return ResponseEntity.ok(new ApiResponse("Application withdrawn successfully", true));
    }

    // ─── Employer APIs ─────────────────────────────────────────────────────────

    @GetMapping("/job/{jobId}")
    public ResponseEntity<Page<ApplicationSummaryResponse>> getApplicationsForJob(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long jobId,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(
                applicationService.getApplicationsForJob(employerId, jobId, status, pageable)
        );
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> getApplicationDetail(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(applicationService.getApplicationDetail(employerId, applicationId));
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long applicationId,
            @RequestBody @Valid UpdateApplicationStatusRequest request
    ) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(employerId, applicationId, request));
    }

    @PatchMapping("/{applicationId}/shortlist")
    public ResponseEntity<ApplicationResponse> toggleShortlist(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(applicationService.toggleShortlist(employerId, applicationId));
    }

    @PatchMapping("/{applicationId}/view")
    public ResponseEntity<ApiResponse> markAsViewed(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long applicationId
    ) {
        applicationService.markAsViewed(employerId, applicationId);
        return ResponseEntity.ok(new ApiResponse("Marked as viewed", true));
    }

    @GetMapping("/job/{jobId}/shortlisted")
    public ResponseEntity<List<ApplicationSummaryResponse>> getShortlisted(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long jobId
    ) {
        return ResponseEntity.ok(applicationService.getShortlistedForJob(employerId, jobId));
    }
}