package com.portal.job.mapper;

import com.portal.job.modal.ApplicationNote;
import com.portal.job.modal.JobApplication;
import com.portal.job.payload.ApplicationNoteResponse;
import com.portal.job.payload.ApplicationResponse;
import com.portal.job.payload.ApplicationSummaryResponse;

public class ApplicationMapper {

    private ApplicationMapper() {}

    public static ApplicationResponse toResponse(JobApplication app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .candidateId(app.getCandidateId())
                .jobId(app.getJobId())
                .employerId(app.getEmployerId())
                .companyId(app.getCompanyId())
                .resumeId(app.getResumeId())
                .status(app.getStatus())
                .coverLetter(app.getCoverLetter())
                .aiScore(app.getAiScore())
                .aiScreeningSummary(app.getAiScreeningSummary())
                .viewed(app.getViewed())
                .shortlisted(app.getShortlisted())
                .additionalAnswers(app.getAdditionalAnswers())
                .appliedAt(app.getAppliedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }

    public static ApplicationSummaryResponse toSummary(JobApplication app) {
        return ApplicationSummaryResponse.builder()
                .id(app.getId())
                .candidateId(app.getCandidateId())
                .jobId(app.getJobId())
                .companyId(app.getCompanyId())
                .status(app.getStatus())
                .viewed(app.getViewed())
                .shortlisted(app.getShortlisted())
                .aiScore(app.getAiScore())
                .appliedAt(app.getAppliedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }

    public static ApplicationNoteResponse toNoteResponse(ApplicationNote note) {
        return ApplicationNoteResponse.builder()
                .id(note.getId())
                .applicationId(note.getApplication().getId())
                .authorId(note.getAuthorId())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }
}