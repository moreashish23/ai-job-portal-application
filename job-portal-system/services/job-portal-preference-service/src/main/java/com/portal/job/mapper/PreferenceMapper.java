package com.portal.job.mapper;

import com.portal.job.modal.AlertFrequency;
import com.portal.job.modal.JobAlert;
import com.portal.job.modal.SavedJob;
import com.portal.job.payload.CreateJobAlertRequest;
import com.portal.job.payload.JobAlertResponse;
import com.portal.job.payload.SavedJobResponse;
import com.portal.job.payload.UpdateJobAlertRequest;

public class PreferenceMapper {

    private PreferenceMapper() {}

    public static SavedJobResponse toSavedJobResponse(SavedJob savedJob) {
        return SavedJobResponse.builder()
                .id(savedJob.getId())
                .candidateId(savedJob.getCandidateId())
                .jobId(savedJob.getJobId())
                .note(savedJob.getNote())
                .savedAt(savedJob.getSavedAt())
                .build();
    }

    public static JobAlertResponse toAlertResponse(JobAlert alert) {
        return JobAlertResponse.builder()
                .id(alert.getId())
                .candidateId(alert.getCandidateId())
                .alertName(alert.getAlertName())
                .keyword(alert.getKeyword())
                .location(alert.getLocation())
                .jobType(alert.getJobType())
                .workMode(alert.getWorkMode())
                .experienceLevel(alert.getExperienceLevel())
                .categoryId(alert.getCategoryId())
                .minSalary(alert.getMinSalary())
                .maxSalary(alert.getMaxSalary())
                .frequency(alert.getFrequency())
                .isActive(alert.getIsActive())
                .lastTriggeredAt(alert.getLastTriggeredAt())
                .createdAt(alert.getCreatedAt())
                .updatedAt(alert.getUpdatedAt())
                .build();
    }

    public static JobAlert toEntity(Long candidateId, CreateJobAlertRequest req) {
        return JobAlert.builder()
                .candidateId(candidateId)
                .alertName(req.getAlertName())
                .keyword(req.getKeyword())
                .location(req.getLocation())
                .jobType(req.getJobType())
                .workMode(req.getWorkMode())
                .experienceLevel(req.getExperienceLevel())
                .categoryId(req.getCategoryId())
                .minSalary(req.getMinSalary())
                .maxSalary(req.getMaxSalary())
                .frequency(req.getFrequency() != null ? req.getFrequency() : AlertFrequency.DAILY)
                .isActive(true)
                .build();
    }

    public static void applyUpdate(JobAlert alert, UpdateJobAlertRequest req) {
        if (req.getAlertName() != null) alert.setAlertName(req.getAlertName());
        if (req.getKeyword() != null) alert.setKeyword(req.getKeyword());
        if (req.getLocation() != null) alert.setLocation(req.getLocation());
        if (req.getJobType() != null) alert.setJobType(req.getJobType());
        if (req.getWorkMode() != null) alert.setWorkMode(req.getWorkMode());
        if (req.getExperienceLevel() != null) alert.setExperienceLevel(req.getExperienceLevel());
        if (req.getCategoryId() != null) alert.setCategoryId(req.getCategoryId());
        if (req.getMinSalary() != null) alert.setMinSalary(req.getMinSalary());
        if (req.getMaxSalary() != null) alert.setMaxSalary(req.getMaxSalary());
        if (req.getFrequency() != null) alert.setFrequency(req.getFrequency());
    }
}