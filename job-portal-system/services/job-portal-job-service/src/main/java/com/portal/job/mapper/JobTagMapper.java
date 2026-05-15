package com.portal.job.mapper;

import com.portal.job.dto.response.JobTagResponse;
import com.portal.job.modal.JobTag;

public class JobTagMapper {

    public static JobTagResponse toTagResponse(JobTag jobTag) {

        return JobTagResponse.builder()
                .id(jobTag.getId())
                .name(jobTag.getName())
                .slug(jobTag.getSlug())
                .build();
    }
}
