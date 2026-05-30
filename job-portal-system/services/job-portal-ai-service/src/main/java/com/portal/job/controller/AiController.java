package com.portal.job.controller;

import com.portal.job.payload.request.*;
import com.portal.job.payload.response.*;
import com.portal.job.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    // ── 1. Generate Job Description ────────────────────────────────────────────
    // Called by employers when creating a job post
    @PostMapping("/job-description")
    public ResponseEntity<AiTextResponse> generateJobDescription(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid GenerateJobDescriptionRequest request
    ) {
        return ResponseEntity.ok(aiService.generateJobDescription(request));
    }

    // ── 2. Generate Cover Letter ───────────────────────────────────────────────
    // Called by candidates during the job application flow
    @PostMapping("/cover-letter")
    public ResponseEntity<AiTextResponse> generateCoverLetter(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid GenerateCoverLetterRequest request
    ) {
        return ResponseEntity.ok(aiService.generateCoverLetter(request));
    }

    // ── 3. Score Candidate ────────────────────────────────────────────────────
    // Called by application-service after a candidate applies
    // (or by employers from their dashboard to trigger AI screening)
    @PostMapping("/score-candidate")
    public ResponseEntity<CandidateScoreResponse> scoreCandidate(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid ScoreCandidateRequest request
    ) {
        return ResponseEntity.ok(aiService.scoreCandidate(request));
    }

    // ── 4. Optimize Resume ─────────────────────────────────────────────────────
    // Called by candidates from resume builder page
    @PostMapping("/optimize-resume")
    public ResponseEntity<AiTextResponse> optimizeResume(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid OptimizeResumeRequest request
    ) {
        return ResponseEntity.ok(aiService.optimizeResume(request));
    }

    // ── 5. Natural Language Job Search ────────────────────────────────────────
    // Called by frontend when user types a natural language query
    // Returns structured parameters that frontend passes to job-service search
    @PostMapping("/search")
    public ResponseEntity<JobSearchQueryResponse> parseJobSearch(
            @RequestBody @Valid NaturalLanguageJobSearchRequest request
    ) {
        return ResponseEntity.ok(aiService.parseNaturalLanguageSearch(request));
    }
}