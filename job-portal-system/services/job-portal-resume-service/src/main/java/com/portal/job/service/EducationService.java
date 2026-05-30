package com.portal.job.service;

import com.portal.job.dto.response.EducationResponse;
import com.portal.job.payload.AddEducationRequest;

import java.util.List;

public interface EducationService {

    EducationResponse addEducation(
            Long resumeId,
            Long candidateId,
            AddEducationRequest request
    );

    List<EducationResponse> getEducations(Long resumeId);

    EducationResponse updateEducation(
            Long educationId,
            Long resumeId,
            Long candidateId,
            AddEducationRequest req
    ) ;

    void deleteEducation(
            Long educationId,
            Long resumeId,
            Long candidateId
    ) ;
}
