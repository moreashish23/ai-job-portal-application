package com.portal.job.controller;

import com.portal.job.dto.response.ApiResponse;
import com.portal.job.payload.SaveJobRequest;
import com.portal.job.payload.SavedJobResponse;
import com.portal.job.service.SavedJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/saved-jobs")
@RequiredArgsConstructor
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping
    public ResponseEntity<SavedJobResponse> saveJob(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid SaveJobRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedJobService.saveJob(candidateId, request));
    }

    @GetMapping
    public ResponseEntity<Page<SavedJobResponse>> getSavedJobs(
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(savedJobService.getSavedJobs(candidateId, pageable));
    }

    @GetMapping("/check/{jobId}")
    public ResponseEntity<Map<String, Boolean>> isJobSaved(
            @RequestHeader("X-User-Id") Long candidateId,
            @PathVariable Long jobId
    ) {
        boolean saved = savedJobService.isJobSaved(candidateId, jobId);
        return ResponseEntity.ok(Map.of("saved", saved));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse> unsaveJob(
            @RequestHeader("X-User-Id") Long candidateId,
            @PathVariable Long jobId
    ) {
        savedJobService.unsaveJob(candidateId, jobId);
        return ResponseEntity.ok(new ApiResponse("Job removed from saved list", true));
    }
}