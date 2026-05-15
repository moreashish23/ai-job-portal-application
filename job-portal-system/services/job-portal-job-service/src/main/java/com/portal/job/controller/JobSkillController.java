package com.portal.job.controller;

import com.portal.job.dto.response.ApiResponse;
import com.portal.job.dto.response.JobSkillResponse;
import com.portal.job.payload.JobSkillRequest;
import com.portal.job.service.JobSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-skills")
public class JobSkillController {

    private final JobSkillService jobSkillService;

    @PostMapping
    public ResponseEntity<JobSkillResponse> createSkill(
            @RequestBody @Valid JobSkillRequest jobSkillRequest
    ) throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobSkillService.createSkill(jobSkillRequest));
    }

    @GetMapping
    public ResponseEntity<List<JobSkillResponse>> getAllSkills() {

        return ResponseEntity.ok(jobSkillService.getAllSkills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobSkillResponse> getSkillById(
            @PathVariable Long id
    ) throws Exception {

        return ResponseEntity.ok(jobSkillService.getSkillById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobSkillResponse> updateSkill(
            @PathVariable Long id,
            @RequestBody @Valid JobSkillRequest req
    ) throws Exception {

        return ResponseEntity.ok(jobSkillService.updateSkill(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSkill(
            @PathVariable Long id
    ) throws Exception {

        jobSkillService.deleteSkill(id);

        return ResponseEntity.ok(
                new ApiResponse("Skill deleted successfully", true)
        );
    }
}
