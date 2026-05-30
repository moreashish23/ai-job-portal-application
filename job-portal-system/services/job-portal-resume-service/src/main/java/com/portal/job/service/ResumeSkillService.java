package com.portal.job.service;


import com.portal.job.dto.response.ResumeSkillResponse;
import com.portal.job.payload.AddResumeSkillRequest;

import java.util.List;

public interface ResumeSkillService {

    ResumeSkillResponse addSkill(
            Long resumeId,
            Long candidateId,
            AddResumeSkillRequest req
    ) ;
    List<ResumeSkillResponse> getSkills(
            Long resumeId
    );

    ResumeSkillResponse updateSkill(
            Long skillId,
            Long resumeId,
            Long candidateId,
            AddResumeSkillRequest req
    ) ;

    void deleteSkill(
            Long skillId,
            Long resumeId,
            Long candidateId
    ) ;
}
