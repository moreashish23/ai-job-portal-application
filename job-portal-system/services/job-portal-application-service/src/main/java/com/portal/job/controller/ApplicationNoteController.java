package com.portal.job.controller;

import com.portal.job.dto.response.ApiResponse;
import com.portal.job.payload.AddNoteRequest;
import com.portal.job.payload.ApplicationNoteResponse;
import com.portal.job.service.ApplicationNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/notes")
@RequiredArgsConstructor
public class ApplicationNoteController {

    private final ApplicationNoteService noteService;

    @PostMapping
    public ResponseEntity<ApplicationNoteResponse> addNote(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long applicationId,
            @RequestBody @Valid AddNoteRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteService.addNote(employerId, applicationId, request));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationNoteResponse>> getNotes(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(noteService.getNotesForApplication(employerId, applicationId));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponse> deleteNote(
            @RequestHeader("X-User-Id") Long employerId,
            @PathVariable Long applicationId,
            @PathVariable Long noteId
    ) {
        noteService.deleteNote(employerId, noteId);
        return ResponseEntity.ok(new ApiResponse("Note deleted successfully", true));
    }
}