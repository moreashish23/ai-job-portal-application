package com.portal.job.controller;

import com.portal.job.dto.response.ApiResponse;
import com.portal.job.dto.response.JobTagResponse;
import com.portal.job.payload.JobTagRequest;
import com.portal.job.service.JobTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-tags")
public class JobTagController {

    private final JobTagService jobTagService;

    @PostMapping
    public ResponseEntity<JobTagResponse> createJobTag(
            @RequestBody @Valid JobTagRequest jobTagRequest
    )   {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobTagService.createTag(jobTagRequest));
    }

    @GetMapping
    public ResponseEntity<List<JobTagResponse>> getAllTags() {

        return ResponseEntity.ok(jobTagService.getAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobTagResponse> getTagById(
            @PathVariable Long id
    )   {

        return ResponseEntity.ok(jobTagService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobTagResponse> updateTag(
            @PathVariable Long id,
            @RequestBody @Valid JobTagRequest req
    )   {

        return ResponseEntity.ok(jobTagService.updateTag(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTag(
            @PathVariable Long id
    )   {

        jobTagService.deleteTag(id);

        return ResponseEntity.ok(
                new ApiResponse("Tag deleted successfully", true)
        );
    }
}