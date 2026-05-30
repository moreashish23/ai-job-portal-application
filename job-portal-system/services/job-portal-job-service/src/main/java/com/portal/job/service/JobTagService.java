package com.portal.job.service;

import com.portal.job.dto.response.JobTagResponse;
import com.portal.job.modal.JobTag;
import com.portal.job.payload.JobTagRequest;

import java.util.List;
import java.util.Set;

public interface JobTagService {

    JobTagResponse createTag(JobTagRequest req) ;

    List<JobTagResponse> getAllTags();

    JobTagResponse getById(Long id) ;

    JobTagResponse updateTag(Long id, JobTagRequest req) ;

    void deleteTag(Long id);

    JobTag getTagEntityById(Long id) ;

    Set<JobTag> getTagsByIds(Set<Long> ids) ;

}
