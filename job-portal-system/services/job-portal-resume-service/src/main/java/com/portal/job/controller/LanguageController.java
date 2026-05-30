package com.portal.job.controller;

import com.portal.job.dto.response.ApiResponse;
import com.portal.job.dto.response.LanguageResponse;
import com.portal.job.payload.AddLanguageRequest;
import com.portal.job.service.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes/{resumeId}/languages")
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<LanguageResponse> addLanguage(
            @PathVariable Long resumeId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddLanguageRequest addLanguageRequest
    ) {

        return ResponseEntity.ok(
                languageService.addLanguage(
                        resumeId,
                        candidateId,
                        addLanguageRequest
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<LanguageResponse>> getLanguages(
            @PathVariable Long resumeId
    )  {

        return ResponseEntity.ok(
                languageService.getLanguages(resumeId)
        );
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<LanguageResponse> updateLanguage(
            @PathVariable Long resumeId,
            @PathVariable Long languageId,
            @RequestHeader("X-User-Id") Long candidateId,
            @RequestBody @Valid AddLanguageRequest req
    )  {

        return ResponseEntity.ok(
                languageService.updateLanguage(
                        languageId,
                        resumeId,
                        candidateId,
                        req
                )
        );
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<ApiResponse> deleteLanguage(
            @PathVariable Long resumeId,
            @PathVariable Long languageId,
            @RequestHeader("X-User-Id") Long candidateId
    ) {

        languageService.deleteLanguage(
                languageId,
                resumeId,
                candidateId
        );

        return ResponseEntity.ok(
                new ApiResponse(
                        "Language deleted successfully",
                        true
                )
        );
    }
}
