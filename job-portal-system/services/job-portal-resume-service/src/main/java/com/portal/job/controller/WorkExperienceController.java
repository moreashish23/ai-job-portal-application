package com.portal.job.controller;

import com.portal.job.dto.response.ApiResponse;
import com.portal.job.dto.response.WorkExperienceResponse;
import com.portal.job.payload.AddWorkExperience;
import com.portal.job.service.WorkExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes/{resumeId}/work-experiences")
@RequiredArgsConstructor
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @PostMapping
    public ResponseEntity<WorkExperienceResponse> addWorkExperience(

            @PathVariable Long resumeId,

            @RequestHeader("X-User-Id")
            Long candidateId,

            @RequestBody @Valid
            AddWorkExperience req

    )  {

        return ResponseEntity.ok(

                workExperienceService.addWorkExperience(
                        resumeId,
                        candidateId,
                        req
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<WorkExperienceResponse>> getWorkExperiences(

            @PathVariable Long resumeId

    ) {

        return ResponseEntity.ok(

                workExperienceService.getWorkExperiences(
                        resumeId
                )
        );
    }

    @PutMapping("/{experienceId}")
    public ResponseEntity<WorkExperienceResponse> updateWorkExperience(

            @PathVariable Long resumeId,

            @PathVariable Long experienceId,

            @RequestHeader("X-User-Id")
            Long candidateId,

            @RequestBody @Valid
            AddWorkExperience req

    ) {

        return ResponseEntity.ok(

                workExperienceService.updateWorkExperience(
                        resumeId,
                        candidateId,
                        experienceId,
                        req
                )
        );
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<ApiResponse> deleteWorkExperience(

            @PathVariable Long resumeId,

            @PathVariable Long experienceId,

            @RequestHeader("X-User-Id")
            Long candidateId

    ) {

        workExperienceService.deleteWorkExperience(
                resumeId,
                experienceId,
                candidateId
        );

        return ResponseEntity.ok(

                new ApiResponse(
                        "Work experience deleted successfully",
                        true
                )
        );
    }
}