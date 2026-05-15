package com.portal.job.mapper;

import com.portal.job.dto.response.JobSkillResponse;
import com.portal.job.modal.JobSkill;

public class JobSkillMapper {

    public static JobSkillResponse toJobSkillResponse(JobSkill skill) {

        return JobSkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .slug(skill.getSlug())
                .category(skill.getCategory())
                .active(skill.getActive())
                .build();
    }
}
