package com.portal.job.service;

import com.portal.job.dto.response.PersonalInfoResponse;
import com.portal.job.dto.response.ResumeResponse;
import com.portal.job.modal.Resume;
import com.portal.job.payload.CreateResumeRequest;

import java.util.List;

public interface ResumeService {

    ResumeResponse createResume(Long candidateId, CreateResumeRequest req);

    ResumeResponse getResumeById(Long resumeId, Long candidateId) ;

    List<ResumeResponse> getMyResumes(Long candidateId);

    ResumeResponse updatePersonalInfo(Long resumeId, Long candidateId, PersonalInfoResponse req) ;

    ResumeResponse updateSummary(Long resumeId, Long candidateId, String summary) ;

    ResumeResponse setDefaultResume(Long resumeId, Long candidateId);

    void deleteResume(Long resumeId, Long candidateId);

    Resume getResumeEntity(Long resumeId);

}