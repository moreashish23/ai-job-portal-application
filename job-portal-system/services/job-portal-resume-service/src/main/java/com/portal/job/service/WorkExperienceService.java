package com.portal.job.service;

import com.portal.job.dto.response.WorkExperienceResponse;
import com.portal.job.modal.WorkExperience;
import com.portal.job.payload.AddWorkExperience;

import java.util.List;

public interface WorkExperienceService {

    WorkExperienceResponse addWorkExperience(
            Long resumeId,
            Long candidateId,
            AddWorkExperience req
    ) ;

    List<WorkExperienceResponse> getWorkExperiences(Long resumeId);

    WorkExperienceResponse updateWorkExperience(
            Long resumeId,
            Long  candidateId,
            Long workExperienceId,
            AddWorkExperience req
    ) ;

    void deleteWorkExperience(
            Long resumeId,
            Long workExperienceId,
            Long candidateId
    ) ;

    WorkExperience getWorkExperienceEntity(Long workExperienceId);
}
