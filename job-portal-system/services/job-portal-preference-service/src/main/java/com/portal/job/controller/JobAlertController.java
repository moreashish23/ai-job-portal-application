package com.portal.job.controller;

import com.portal.job.dto.response.ApiResponse;
import com.portal.job.payload.CreateJobAlertRequest;
import com.portal.job.payload.JobAlertResponse;
import com.portal.job.payload.UpdateJobAlertRequest;
import com.portal.job.service.JobAlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job-alerts")
@RequiredArgsConstructor
public class JobAlertController {

    private final JobAlertService jobAlertService;

    @PostMapping
    public ResponseEntity<JobAlertResponse> createAlert(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid CreateJobAlertRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobAlertService.createAlert(candidateId, request));
    }

    @GetMapping
    public ResponseEntity<Page<JobAlertResponse>> getMyAlerts(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(jobAlertService.getMyAlerts(candidateId, pageable));
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<JobAlertResponse> getAlertById(
            @RequestHeader("X-User-Id") Long candidateId,
            @PathVariable Long alertId
    ) {
        return ResponseEntity.ok(jobAlertService.getAlertById(candidateId, alertId));
    }

    @PutMapping("/{alertId}")
    public ResponseEntity<JobAlertResponse> updateAlert(
            @RequestHeader("X-User-Id") Long candidateId,
            @PathVariable Long alertId,
            @RequestBody @Valid UpdateJobAlertRequest request
    ) {
        return ResponseEntity.ok(jobAlertService.updateAlert(candidateId, alertId, request));
    }

    @PatchMapping("/{alertId}/toggle")
    public ResponseEntity<JobAlertResponse> toggleAlert(
            @RequestHeader("X-User-Id") Long candidateId,
            @PathVariable Long alertId
    ) {
        return ResponseEntity.ok(jobAlertService.toggleAlert(candidateId, alertId));
    }

    @DeleteMapping("/{alertId}")
    public ResponseEntity<ApiResponse> deleteAlert(
            @RequestHeader("X-User-Id") Long candidateId,
            @PathVariable Long alertId
    ) {
        jobAlertService.deleteAlert(candidateId, alertId);
        return ResponseEntity.ok(new ApiResponse("Job alert deleted", true));
    }
}